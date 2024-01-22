# databases-course

## DDL-schema

In het schema in de repository zijn een aantal belangrijke sleutelelementen terug te vinden die uitleggen hoe voor het VGHF een database ontworpen zou kunnen worden. Wij hebben ervoor gekozen om dit te doen voor het project: Research Library. Om te beginnen worden de verschillend entiteiten gedefinieerd die van belang zijn om een constructieve database te maken. Enkele zaken die belangrijk zijn om bij te houden zijn:
- De musea
- Bezoekers aan deze musea
- De boeken die aanwezig zijn in het assortiment
- De aanwezige games
- Mogelijke bijdrages, dit zouden eventuele donaties kunnen zijn of andere kosten
- Uitleningen

Om te beginnen willen we bijhouden welke musea het VGHF heeft en mogelijk haar opbrengsten uit bijdrages. Elke entiteit heeft ID en dus heeft een museum dit ook, net zoals alle andere entiteiten die worden bijgehouden. Andere nuttige eigenschappen kunnen de naam en het adres van een bepaald museum zijn, zodat medewerkers van het VGHF dit snel kunnen opzoeken wanneer het nodig is. Er zijn talloze andere eigenschappen die gelist kunnen worden zoals telefoonnummers, een btw-nummer, aantal medewerkers, financiele cijfers, etc.. maar voor de scope van de taak lijkt het te volstaan om vooral de voornaamste eigenschappen bij te houden en andere trivialiteiten voorlopig te negeren.
Het ID van elke entiteit is uniek en wordt gemakkelijkheidshalve bijgehouden met behulp van een Integer. De datafields "naam" en "adres" bevatten alfabetische karakters en zullen dus in de vorm van een String bijgehouden worden. De opbrengst en financiele bedragen worden met een Double bijgehouden omdat we komma-getallen willen gebruiken. Een Double of BigDecimal zouden echter ook perfect gebruikt kunnen worden. 
Men kan ook bepaalde integriteitsregels of constraints afdwingen. Een land moet bijvoorbeeld gelijk zijn aan een van de 196 erkende landen. De opbrengst mag slechts 2 getallen na de komma hebben. Een jaartal moet uit 4 realistische cijfers bestaan.

Deze eigenschappen zijn gelijkaardig voor alle andere entiteiten. Een uitzondering hierop is bijvoorbeeld in "Bijdrage", waar voor de betaalmethode uit een enum gekozen kan worden. Dit zijn vooraf gedefinieerde waardes die in dit geval bijvoorbeeld: contant, Bancontact, Visa of PayPal zouden kunnen zijn.
In de Game-tabel wordt ook gebruik gemaakt van de enum. In dit geval zou gekozen kunnen worden voor bijvoorbeeld: pc, Playstation, Xbox, Atari 2600, Nintendo 64, Sega Saturn, etc... Het gebruik van enums zorgt voor een strictere categorisatie waardoor het terugvinden van bepaalde spellen voor een bepaalde console later eenvoudiger zou kunnen worden.

Naast enkel de tabeleigenschappen of velden kunnen meerdere tabellen ook in relatie staan tot elkaar. De voornaamste onderverdeling die we kunnen maken in dit geval zijn de een-op-veel of veel-op-veel relaties. 

Een 1-op-1 relatie kan ook beschouwd worden tussen de Bijdrage en BijdrageTijdensBezoek, het zou immers uitzonderlijk zijn dat een bezoeker tweemalig een bijdrage uitvoerd tijdens een enkel bezoek. Dit kan echter omslachtig of inefficiënt zijn, maar kan ook een zekere orde brengen en gemakkelijker leesbaar zijn.

In een een-op-veel relatie kan een enkel element met een hele verzameling in relatie staan. Zo kan 1 bezoeker bijvoorbeeld meerdere games uitlenen. Maar elke game slechts aan een bezoeker tegelijkertijd uitgeleend worden.

Een bezoeker kan ook besluiten om naar meerdere musea te gaan terwijl een museum gewoonlijk wel meerdere bezoekers op een dag binnenkrijgt. In dit geval spreken we over een veel-op-veel relatie. Om dingen te vereenvoudigen maken we in dit geval een tabel Bezoek waarin de ID van de bezoeker en het museum meegegeven worden, alsook de datum van het bezoek en een mogelijke bijdrage tijdens het bezoek.
Een boek kan in een langere periode aan meerdere bezoekers worden uitgeleend, terwijl een bezoeker meerdere boeken kan uitlenen. Dit is analoog aan de relatie tussen bezoekers en musea en wordt dus ook beschreven door de veel-op-veel relatie. Een tussentabel GeleendBoek wordt hiervoor aangemaakt. Deze houdt dan ook bij op welk bezoek de uitlening van start gegaan is. Dezelfde werkwijze wordt gehaneerd voor games die uitgeleend worden.
In de implementatie van deze relaties wordt gepoogd om de integriteit van de de database te garanderen en er wordt voor gezorgd dat alleen geldige relaties gevormd kunnen worden.
Een mogelijke manier van het schrijven 
