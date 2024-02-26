# KotlinWeb
by Allround

## Beschreibung
KotlinWeb ist ein Webframework für Kotlin. 
Das Framework bietet eine einfache Möglichkeit, um auf Requests zu reagieren.
Basieren tut es dabei auf Vert.x Web, was zu einer schnellen Performance führt.

## Anwendungszwecke
- REST Apis
- Server Side Rendered Web Anwendungen

## Vorteile
- Der direkte Zusammenschluss von Backend und Frontend sorgt für eine vereinfachte Beziehung zwischen beiden Instancen
- Alle Benefits von Vert.x Web (Sessions, Cookies, Request + Response Parsing, Json API,...)
- Leicht zu verstehendes Api Design
- Einfache und dynamische Einbindung von HTMX
- Automatische CSS Style generierung (Redesign in Arbeit)
- Action/Script System (In Planung)

## Nachteile
- Nur für SSR ausgelegt
- Nicht für kompliziertes Client Side JavaScript gedacht

## Beispiele
Für Beispiele kannst du dir die Example.kt Datei im Test Ordner angucken.