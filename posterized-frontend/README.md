## Priprema za deploy na Render:
- u package.json dodati dependencye potrebne za deploy, primarno http-proxy-middleware, dotenv, express
- dodati /src/setupProxy.js koji služi kao proxy server za lokalni development (redirecta api pozive na localhost:8080), odnosno kad se koristi `react-scripts start` skripta
- dodati app.js, u kojem se nalazi express server za produkcijski proxy i posluzivanje frontenda
- u package.json izmijeniti sljedeću skriptu:

    `"build": "yarn install && react-scripts build"`
- i dodati:

    `"start-prod": "node app.js"`

  - u package.json dodati sljedeći odjeljak zbog Node verzije:
  ```
  "engines": {
      "node": ">=18.18.0 <19.0.0"
      },
  ```
- koristenje proxya omogucuje da se pozivi na api izvrsavaju bez potrebe za eksplicitnim pozivom adrese backenda - pogledati App.tsx za primjer

### Kreiranje frontenda:
U Render dashboardu:
- New -> Web Service
- Povezati GitHub račun, nakon cega su za odabir dostupni svi projekti na koje imate prava pristupa
- Stisnuti connect pored odgovarajućeg projekta
- Postaviti ime za servis (postat će dio web adrese)
- Root directory ostaviti prazan
- Environment Node
- Region Frankfurt
- Build Command postaviti na `yarn build`, a Start Command `yarn start-prod`
- Na dnu prosiriti _advanced_
- Dodati potrebne environment varijable - API_BASE_URL postaviti na adresu deployanog backenda aplikacije dostupnu na Render dashboardu
- Stisnuti Create Web Service

Napomena: U free tier-u nakon perioda neaktivnosti aplikacije na Renderu bit će automatski ugašene, te ponovno podignute pri prvom zaprimanju zatjeva (otvaranje web stranice frontenda ili slanje zahtjeva na API).
Ovo može rezultirati s nekoliko minuta nedostupnosti aplikacije ako se otvara npr. prvi put u danu.