TODO : megirni ezt a script-et Omnigraffle abraban (esetleg ???)


Szereplok:
  - **Titkarno**
  
  - **Fonok** :   
    - rendereles parancsot kiado.
    - titkarnonek adja ki.


  - **Festok** :
    - mit csinalnak, ha jon a titkarno es megkerdezi toluk, hogy mit akarnak lefesteni ?
         - megmondjak, le akarnak festeni valamit, 
           de ahhoz, hogy azt le tudjak festeni, ket dologra van szukseguk:
            - nem tudjak pontosan mit kell lefesteniuk
            - nem tudjak, hogy a festmeny nehany reszet, hogyan kell lefesteni, 
              meg akkor sem, ha tudjak, hogy mirol kell szolnia azoknak a reszeknek. 
            - Ezert ezeket az al reszeket kiadja specialistaknak es ad hozza utasitast, hogy mit fessenek le.
         - megmondja a titkarnonek, hogy itt van 10 level, amit le kell szallitani tiz beosztottjanak, 
           akik majd a levelek alapjan, megmondjak, hogy mit akarnak rendelni. Ezeknek a beosztottaknak, 
           lehetnek ujabb beosztottjaik, akik megint adhatnak leveleket es cimeket a titkarnonek.
    - mit adnak vissza


Targyak, fogalmak:
  - **Kep** (VDOM) <br>
    Nem tartalmaz semmi "utasitast". Egy sima, renderelheto kep.
  - **Munkaterv**
    - tartalmaz :
      - Keplista : lista mar vegleg elkeszult kepekrol (VDOM)
      - Nem aláírt szerződések alvállalkozókkal. (Amik még nincsenek aláírva.)
        - Ez egy lista arrol, hogy melyik festővel, mit kell lefestetni és hogy
          melyik al-festőnek mi "lesz mondva", azaz, ha pl. egy alvállakozó
          angyal festésben profi, akkor ő meg fogja kérdezni, hogy milyen angyalt
          akarunk, hogy lefessen nekünk : arkangyalt vagy földre hullott angyalt?
          
        - Az alvállalkozó visszaadhat vagy egy `Terv`-et, vagy pedig
         
      - Osszeszerelesi utmutato: 
          megmondja, hogy ha valaki a Tervlista-bol keszit egy Keplista-t, akkor
          azokat, hogyan kell osszerakni egy Keppe (VDOM)
          
  - Alvállalkozóknak leadható megrendelésben az alvállalkozó által feltett **kérdések**
    és a **válaszok** arra.
    
  - Festok **emlekezete**
    Bizonyos helyzetekben, ha megint le kell festeni egy festményt, csak egy kicsit más fajtát,
    akkor lehet az, hogy a már meglévő **Munkaterv**-ben szereplő főfestő és az alvállalkozók
    "nem lesznek elfelejtve", úgymond, lesz nevük. És így lehet az, hogy lesz "memóriájuk":
     - azaz nem csak attól függhet, hogy mit csinál az alvállalkozó, hogy a főnök
       mit mondott neki (props), hanem emlékezhet valamire az előző renderelésből és ezt a valamit
       valami meg tudja változtatni. Ahol ez valami az nem a főnök.
    


Folyamat:

  - Rendereles :

    - Odamegy a titkarno a fő festőhöz és aszondja neki: 
       - Főfestő fess valami szépet a Királynak!
       - Jó, fel tudok ajánlani néhány lehetőséget, ami alapján
           meg tudom festeni a Király kedvenc képét. A következő 
           kérdésekre kéne a Királynak válaszolnia.
           - Kérdések (paraméterek, prop-ok):
             - Miről szóljon a kép ?
             - Milyen évszakban "legyen" ?
             - Hol legyen ?
             - Mikor legyen ?
             - Mekkora legyen ?
             - Kinek lesz ? (Esetleg ajándék egy másik uralkodónak ?)
       - Oké megkérdezem a Királyt.
    
    - Jön vissza a titkárnő a királytól a válaszokkal. Odaadja a főfestőnek.
    
    - A főfestő nekiáll festegetni és amit nem tud megfesteni, azt kiadja
      alvállalkozó festőknek és megmondja nekik, hogy mit kell tudniuk, ahhoz,
      hogy megcsinálják a melót, amit kiadott nekik.
      
      - Az alvállalkozó festők, megint kiadhatnak részmunkákat más alvállalkozóknak.
      
      - Ez a folyamat akkor áll meg, mikor már egyik alválalkozó sem fog másik alvállalkozót megkérni,
        hogy segítsen neki.
    
    - A festők (főfestő és alvállalkozók egy része), azok nem készítenek konkrét képet (VDOM),
      hanem csak egy `Terv`-et. 
      
    - Sok festegetés (VDOM) és `Terv` elkészítése után.
    
    - Feljegyzi a titkarno, hogy kinek mi kell,
      es ad mindenkinek egy cetlit a kezebe, arrol, hogy mit kert, hogyha jon
      valaki mas, akkor az illeto meg tudja mutatni a havarjanak, hogy milyen
      csomagra var.

    - A renderelo, szol a beszallitonak, hogy
      osszegyujtotte a listat es ezeket kell, hoznia.
      


- Kess vázlat
  
  - Közelítések (gányolások)
    - Globálisan el lehet érni a kesst
  
  - Szereplők
