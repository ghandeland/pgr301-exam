# Eksamensbesvarelse PGR301 H2021
Kandidatnummer: `2020`

## DevOps-prinsipper og pipeline
#### DevOps-prinsipper og drøfting
GitHub har muligheter for å legge til en rekke forskjellige sjekker og restriksjoner som øker kontrollen på utviklingsprosessen. Branch protection er et verktøy i GitHub som åpner for å legge inn restriksjoner på Git-brancher, og denne beskyttelsen kan ta ugangspunkt i mange forskjellige regler.

Dersom man vil unngå å integrere kode som ikke kompilerer eller passerer tester, er Branch protection med status checks nyttig. Status checks i GitHub gir informasjon om hvorvidt en commit møter kriteriene som er satt for et GitHub repository, og kan forhindre integrering av kode til en gitt branch hvis kriteriene ikke blir møtt. Disse kravene kan innebære alt fra kompilering og testkjøring, til dockerizing av applikasjoner som pushes ut i registre gjennom kontinuerlig integrasjon.

Dersom man skal "beskytte" en Git-branch, er man nødt til å ha noen statussjekker å verifisere mot. GitHub Actions lar de med tilgang på et GitHub-repository, legge til workflows som skal gjennomføres. Hvilken workflow man velger er helt avhengig av hva slags prosjekt man jobber med, og hvilke behov man har. Det finnes en stor mengde ferdiglagde maler å velge mellom, med mange muligheter for konfigurering. I eksempelet under, bruker jeg «Simple workflow» fra GitHub for gjennomgangens skyld, men dette kan eksempelvis være en Maven-workflow som kompilerer og kjører tester i Java. Denne workflow-filen kommer allerede konfigurert for å kjøre både på `push` og `pull request` på main branch.
#### Hvordan legge til workflow
1. I et gitt GitHub repository, velg fanen `Actions`.
2. Naviger til ønsket workflow, og velg `Set up this workflow`.
3. Modifiser `.yml`-filen etter behov, og commit.
![github workflow example](https://github.com/ghandeland/pgr301-exam/blob/main/images/github_workflow_0.png)

En annen måte å få økt kontroll over utviklingsprosessen i GitHub, er å pålegge et visst antall «approvals» på en pull request før den kan integreres inn i en gitt branch. Dette vil si at et visst antall brukere, med tilgang på repository'et, må se over og godkjenne en pull request før den merges. Fordelen med dette er at når flere personer ser over koden, vil det øke sjansen for å finne potensielle bugs eller andre forbedringer.
#### Hvordan sette krav til antall godkjennelser av en pull request 
1.	Naviger til fanen `Settings` og seksjonen `Branches`. Skriv navnet på grenen under `Branch name pattern`.
2.	Velg regelen som heter `Require a pull request before merging`, og velg antall «godkjennere» som må til per pull request. Dersom man ønsker å ha ett annet medlem av teamet til å godkjenne i tillegg til en selv, settes verdien til 2.
3.	Klikk `Save changes` i bunnen av siden. Endringene skal så være lagret, og regelen vil håndheves ved neste pull request.
https://github.com/ghandeland/pgr301-exam/blob/main/images/aws_credentials_1.png
![github workflow example](https://github.com/ghandeland/pgr301-exam/blob/main/images/github_workflow_1.png)

#### Drøftningsoppgave SkalBank
Slik systemutviklingen i SkalBank beskrives i oppgaven, er det mange prinsipper fra DevOps som ikke etterleves. Det at flere utviklere comitter til main branch kontinuerlig, er en svært ineffektiv måte å jobbe på. Det vil føre til problematikk knyttet til merging av kode, og mange uforutsette bugs (som de senere vil ha problemer med å luke ut på grunn av manglende oversikt). Dette vil sannsynligvis heve terskelen drastisk, for å pushe koden ut til produksjonsmiljøet. Det kan dermed ende opp med å bli en veldig omfattende prosess å legge til nye funksjoner i systemet, og de kan til og med ende opp med å være utdaterte når de først kommer ut.

Det at banken har et separat team som kun dedikeres til testing, vil også kunne skape store problemer. Utviklerne har ikke noe «skin in the game», altså har de ikke noe å tape på å skrive dårlig kode. Når bedriften i tillegg har en såpass kaotisk tilnærming til versjonskontroll, vil det gjøre det enda vanskeligere å knytte kritiske feil i koden, tilbake til riktig utvikler. Dermed blir det vanskeligere å kunne ta tak i gjentakende problemer, og det kan resultere i at utviklerne mister insentivet til å skrive robust kode. 

Dersom banken legger mer vekt på automatisering av tester, med høyere kvalitet, vil det frigjøre mye tid. Ressursene som kun blir brukt på manuelle tester, kan brukes på mer produktive oppgaver. De store utviklingsteamene i banken bør splittes til mindre grupper, som jobber separat på hver sin branch. Funksjonene de lager, bør de selv skrive tester til, i tillegg til at den bør kjøres gjennom en omfattende pipeline med generelle end-to-end tester. På grunn av oversikten dette gir, vil koden sannsynligvis testes mer grundig på denne måten.

#### Pipeline
Jeg valgte å bruke workflowen Java CI with Maven som base. På grunn av formålet med pipelinen, ga det mening å også la den kjøre ved pull request. Gjennom `upload-artifact@v2` laster den opp artifakten ved vellykket kjøring, slik at den kan lastes ned fra repository'et, og kjøres lokalt hos de som har tilgang.

## Feedback
Jeg valgte å implementere en `counter`-registrering per endepunkt. I tillegg la jeg inn `@Timed`-annotasjon over hvert endepunkt. Dette gjorde at jeg fikk to separate målinger å gjøre spørringer mot, `backend_exception` og `http_server_requests`. Siden timers også fungerer som counters, kan den brukes til å få oversikt over både antall- og tidsrelatert data.

#### Grafana
![grafana graph](https://github.com/ghandeland/pgr301-exam/blob/main/images/grafana_backendexception.PNG)
 Denne grafen kjører en spørring mot measurement ved navn `backend_exception`, oggrupperer på endepunktene. Grafen gir oversikt over antall ganger hvert av endepunktene har kastet en exceptiion.

![grafana graph](https://github.com/ghandeland/pgr301-exam/blob/main/images/grafana_response_times.PNG)
Spørringen henter inn verdier fra dataen som blir generert av `@Timed`-annotasjonene over endepunktsmetodene. Den grupperer på `endpoint`-taggen, som gir en god oversikt over endepunktenes tidsbruk i forhold til hverandre. Man ser både at responstidene generelt sett er høye, men også at de varierer kraftig. Som forventet, er balance-endepunktet vesentlig tregere enn de andre på grunn av den prekonfigurerte forsinkelsen.

![grafana graph](https://github.com/ghandeland/pgr301-exam/blob/main/images/grafana_request_count.PNG)
Denne kjører spørringer mot ovennevnte Timer-measurement, `http-server-requests`, men henter ut count-verdier istedenfor. Den første spørringen grupperer på endepunkter, mens den andre fungerer som et gjennomsnitt over tid.

![grafana graph](https://github.com/ghandeland/pgr301-exam/blob/main/images/grafana_transfer_request_exception.PNG)
Ved å kombinere ovennevnte teknikker, kjøres det spørringer mot både `backend_exception` og `http-server-requests`. Her får man oversikt over forholdet mellom totalt antall spørringer, og hvor mange exceptions som har blitt kastet, over tid. Som man ser på grafen, er det bare noen ytterst få forespørseler som faktisk blir besvart på riktig måte, som tyder på et svært ustabilt system.

#### Eksmpelspørringer
`SELECT SUM(count) AS successfulReqs FROM "http_server_requests" WHERE ("count" > 0 AND "outcome" = 'SUCCESS')`
Denne spørringen teller antall vellykede forespørseler. Den kan deles på følgende forespørsel (totalt antall forespørseler) og ganges med 100 for å få en prosentandel av vellykede forespørseler.
`SELECT SUM(count) AS totalReqs FROM "http_server_requests" WHERE ("count" > 0)`

For å hente ut antallet forespørsler som har en responstid på over 1 sekund kan man kjøre følgende spørring.
`SELECT SUM("count") FROM http_server_requests WHERE ("count" > 0 AND "sum" > 1000)`

Man kan også spesifisere endepunkt på alle ovennevnte spørringer ved å for eksempel legge til `AND "uri" = '/account'` på slutten av siste parantes.

Som et drøftningsresultat av telemetrien, kan man endelig få en slutt på blamestormingen mellom avdelingene. Konklusjonen er at kjernesystemet, `ReallyShakyBankingCoreSystemService`, forårsaker forsinkelsene. Service-metodene isoleres i `try..catch` for å kunne sette riktige tags i henhold til hvilken metode som feiler. Endepunktsmetodene benytter seg nærmest bare av service-klassen, som gjør at man kan utelukke at andre deler av applikasjonen skaper tregheter.

## Terraform
#### Drøftningsoppgave .tfstate
Terraform bruker «.tfstate»-filer for å blant annet kartlegge fysiske ressurser opp mot Terraform-koden. Det er derfor nødvendig å ha en slik fil, når Terraform skal lage planer og gjøre endringer på infrastrukturen. Når «Jens» kjørte Terraform for første gang, ble state-filen opprettet. Etter sletting, vil Terraform altså miste all oversikt over eksisterende infrastruktur, og dermed klage på at ressursene man prøver å lage allerede eksisterer. I dette tilfellet kan kommandoene `terraform state pull` og `terraform state push` være mulige løsninger. Disse kommandoene henter ut og pusher state-data til og fra en konfigurert backend.

#### Opprettelse av S3 Bucket fra AWS CLI
Dersom sensor ikke har konfigurert AWS credentials, må vedkommende først få dette på plass for å kunne autentisere forespørsler mot AWS CLI. Første steg er å logge inn i AWS Management Console, og logge seg inn med sin IAM-bruker, og navigere til IAM-tjenesten.
![aws example](https://github.com/ghandeland/pgr301-exam/blob/main/images/aws_credentials_0.png)
Deretter navigerer trykker man på `Users` på venstre panel, og går inn på brukeren sin. Under fanen `Security credentials`, skal man trykke på `Create access key`. Man skal da få utlevert en `access key ID` og en `secret access key`. På dette punktet er det viktig å lagre den hemmelige aksessnøkkelen på en trygg måte, fordi den kun vises ved opprettelse.

Deretter kan sensor skrive inn kommandoen `aws configure` i Command Line Interface’et for å så oppgi aksessverdiene, region og ønsket output-format (JSON). Dette vil opprette en config-fil i mappen `~/.aws/config` dersom den ikke eksisterer. 
Gitt at IAM-brukeren har rettighetene til det, skal sensor nå kunne benytte seg av kommando for å lage en S3 bucket via AWS CLI:
`aws s3api create-bucket --bucket mybucket --region eu-west-1`
Teksten som etterfølger `--bucket` flagget vil være navnet på ressursen, og det siste flagget sørger for at den havner i regionen `eu-west-1`. IAM-brukeren som kjører kommandoen vil også stå som eier på ressursen.

#### Bruk av pipeline
Variabeler `ecr_repo_name` og `s3_bucket_name` i `variable.tf` må endres til egne verdier. Verdien i `s3_bucket_name` skal være navnet på S3-bucket'en som ble opprettet i forrige fremgangsbeskrivelse. For oversiktens skyld, skal `.tfstate`-filen pushes til egen mappe. Det må derfor opprettes en mappe ved navn `/tf/` i bøtten. `ecr_repo_name` settes til ønsket navn på Elastic Container Registry.

For å autentisere mot AWS benytter workflow-filen GitHub secrets. Secrets med navnene `AWS_ACCESS_KEY_ID` og `AWS_SECRET_ACCESS_KEY` må legges inn i fork'et repository, med verdier man får uthentet ved å følge fremgangsmåten over. 

## Docker
#### Pipeline
Docker-workflowen bruker GitHub secret `AWS_ECR_REPOSITORY`, som navnet på tidligere opprettet Elastic Container Registry. I likhet med Terraform-workflowen benytter den også secrets `AWS_ACCESS_KEY_ID` og `AWS_SECRET_ACCESS_KEY` for å autentisere mot AWS. Før AWS-tilkoblingen skjer, lagres kortversjonen av Git commit SHA-verdien i variabel `SHORT_SHA`, som senere benyttes til å navngi containeren som pushes til ECR.

#### Kommandoer
Bygg et lokalt image:
`docker build -t pgr301-banking-api .`
Kjør bygget image på port 7777: 
`docker run -p 7777:8080 pgr301-banking-api`
Dersom utviklerne i eksempelbanken ønsker å kjøre enda et image på port `8888`, kan de kjøre følgende kommando: 
`docker run -p 8888:8080 pgr301-banking-api`
 Verdiene etter `-p` flagget mapper porten på lokal maskin til port `8080` i containeren, som er porten til Spring.
