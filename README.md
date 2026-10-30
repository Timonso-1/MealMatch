# MealMatch

Essensplaner für Android und iOS. Jeder User hat einen Account und kann Gerichte einreichen. Die Gerichte werden
zufällig auf die Woche verteilt, und daraus entsteht eine Einkaufsliste. Man kann alles allein nutzen oder
gemeinsam in einer Familie.

- **Frontend:** Kotlin Multiplatform mit Compose Multiplatform (Android und iOS, kein Web), Ktor-Client
- **Backend:** Spring Boot (noch nicht angelegt)

## Projektstruktur

* [/iosApp](./iosApp/iosApp) enthält den iOS-Einstiegspunkt. Die UI kommt aus Compose Multiplatform.
* [/androidApp](./androidApp) enthält den Android-Einstiegspunkt.
* [/shared](./shared/src) enthält den gemeinsamen Code:
  - [commonMain](./shared/src/commonMain/kotlin) für Code, der auf allen Plattformen läuft
  - `androidMain` und `iosMain` für plattformspezifischen Code

Der gemeinsame Code ist nach Features gegliedert:

```
core/network      HTTP-Client, API-Konfiguration, ApiResult
core/ui/theme     Theme
feature/mealplan  domain, data, ui
```

## Entscheidungen

### Bereiche
- Es gibt einen **persönlichen Bereich** (eigene Gerichte, eigener Plan, eigene Einkaufsliste) und **Familien**.
- Ein User kann in **mehreren Familien** sein. Rollen: `ADMIN` und `MEMBER`.
- Beitritt per **E-Mail-Einladung** oder per **Einladungslink** mit maximaler Nutzungszahl. Links sind widerrufbar
  und laufen ab.

### Login
Sehr sicher, in dieser Reihenfolge geplant:
1. E-Mail + Passwort mit E-Mail-Verifizierung, kurzlebige Access-Tokens, Refresh-Tokens mit Rotation
2. Google- und Apple-Login
3. Zwei-Faktor (TOTP)
4. Passkeys

Das Backend wird von Anfang an so gebaut, dass mehrere Login-Wege auf denselben User zeigen.
Ein externer Login darf nur mit einem bestehenden Konto verbunden werden, wenn der Anbieter die E-Mail als
verifiziert meldet.

### Gerichte
- Zutaten sind **Freitext** und je Zutat **Pflicht oder optional** (z. B. Nudeln Pflicht, Salz optional).
- Ein Gericht gehört immer einem User. Es kann zusätzlich in Familien **freigegeben** werden.
- Verlässt jemand eine Familie, bleiben seine freigegebenen Gerichte dort.
- Wird ein Gericht gelöscht, das in einem Plan steht, gibt es vorher eine Warnung. Der Plan-Eintrag bleibt
  danach leer und kann einzeln neu gewürfelt werden.

### Wochenplan
- Ein Gericht pro Tag, Montag bis Sonntag. Der Plan wird **gespeichert**, damit alle denselben sehen.
- Jedes Mitglied darf mischen oder einen einzelnen Tag neu würfeln. Gerichte, die diese Woche schon im Plan
  stehen, werden dabei ausgelassen.
- Gleichzeitige Änderungen werden über eine `version` erkannt (Optimistic Locking). Wer zu spät kommt, bekommt
  den neuen Stand angezeigt.
- Einzelne Tage lassen sich manuell sperren (`locked`).

### Einkaufsliste
- Sie ist **gespeichert** und unabhängig vom Plan. Man kann auch Dinge wie "Waschpulver" eintragen.
- Gleiche Zutaten aus mehreren Gerichten werden zusammengeführt, der Abgleich läuft über `normalized_text`.
- In einer Familie sehen alle live, was abgehakt ist.
- Der **Vorrat** (`pantry_item`) wird beim Erzeugen der Liste übersprungen.
- Wird eine **Pflicht-Zutat** abgehakt, sind alle Plan-Einträge gesperrt, aus denen sie stammt. Nur ein Admin kann
  das erzwingen. Optionale Zutaten sperren nichts. Die Sperre wird aus dem Abhaken **berechnet** und nicht
  gespeichert, damit sie mit dem Haken wieder verschwindet.

## Datenmodell

Alle `id`-Felder sind UUIDs, damit sie nicht erratbar sind.

```
user ──< login_method
user >──< family                         (über family_member, mit Rolle)
family ──< invitation
user ──< meal ──< meal_ingredient
meal >──< family                         (über meal_share)
user/family ──< meal_plan ──< meal_plan_entry >── meal
user/family ──< shopping_list ──< shopping_item
shopping_item >──< meal_plan_entry       (über shopping_item_source)
user/family ──< pantry_item
```

`user/family` heißt: genau eins von `user_id` und `family_id` ist gesetzt, erzwungen durch einen Check Constraint.

### user
| Feld | Sinn |
|---|---|
| `id` | Primärschlüssel |
| `email` | eindeutig |
| `email_verified` | erst nach Bestätigung ist ein Login erlaubt |
| `display_name` | Anzeigename |
| `created_at` | Zeitpunkt |

### login_method
Eine Zeile pro Anmeldemöglichkeit eines Users.

| Feld | Sinn |
|---|---|
| `user_id` | zeigt auf `user` |
| `type` | `PASSWORD`, `GOOGLE`, `APPLE`, `TOTP`, `PASSKEY` |
| Daten je Typ | z. B. Passwort-Hash oder externe Kennung |

### family und family_member
| Tabelle | Felder |
|---|---|
| `family` | `id`, `name`, `created_at` |
| `family_member` | `family_id`, `user_id` (zusammen der Schlüssel), `role`, `joined_at` |

### invitation
| Feld | Sinn |
|---|---|
| `id`, `family_id`, `invited_by` | Verweise |
| `email` | gesetzt bei Mail-Einladung, sonst leer |
| `token_hash` | Hash des geheimen Tokens aus dem Link |
| `max_uses`, `use_count` | Mail-Einladung: 1, offener Link: z. B. 5 |
| `expires_at`, `revoked_at` | Ablauf und Widerruf |

Einlösen in **einer Transaktion**: Token gefunden, nicht widerrufen, nicht abgelaufen, `use_count < max_uses`,
bei gesetzter `email` stimmt die verifizierte E-Mail des Users. Erst dann `use_count` erhöhen und Mitglied anlegen.

### meal, meal_ingredient, meal_share
| Tabelle | Felder |
|---|---|
| `meal` | `id`, `owner_id`, `name`, `notes`, `created_at` |
| `meal_ingredient` | `id`, `meal_id`, `text`, `optional` |
| `meal_share` | `meal_id`, `family_id` (zusammen der Schlüssel) |

### meal_plan und meal_plan_entry
| Tabelle | Felder |
|---|---|
| `meal_plan` | `id`, `user_id` oder `family_id`, `week_start`, `version` |
| `meal_plan_entry` | `plan_id`, `day` (zusammen der Schlüssel), `meal_id` (darf leer sein, `ON DELETE SET NULL`), `locked` |

Pro Besitzer und Woche gibt es nur einen Plan.

### shopping_list, shopping_item, shopping_item_source
| Tabelle | Felder |
|---|---|
| `shopping_list` | `id`, `user_id` oder `family_id` |
| `shopping_item` | `id`, `list_id`, `text`, `normalized_text`, `optional`, `checked_by`, `checked_at` |
| `shopping_item_source` | `item_id`, `plan_entry_id`. Ohne Zeile ist der Eintrag von Hand angelegt |

### pantry_item
`id`, `user_id` oder `family_id`, `normalized_text`.

## Offene Punkte
- Wie optionale Zutaten in der Einkaufsliste angezeigt werden (normal, als Hinweis oder gar nicht).
- Ob Admins Beitritte über offene Links noch bestätigen müssen.
- Ob Backend und Frontend in einem Repo bleiben (z. B. Ordner `backend/`).

## Starten

Nutze die Run-Konfigurationen in der IDE. Alternativ:

- Android-App: `./gradlew :androidApp:assembleDebug`
- iOS-App: Ordner [/iosApp](./iosApp) in Xcode öffnen und von dort starten.

## Tests

- Android: `./gradlew :shared:testAndroidHostTest`
- iOS: `./gradlew :shared:iosSimulatorArm64Test`

---

Mehr zu [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
