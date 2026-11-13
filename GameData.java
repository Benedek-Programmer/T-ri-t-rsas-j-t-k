import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameData {
    // Modern színpaletta (Dark Theme)
    public static final Color BG_DARK = new Color(24, 28, 36);
    public static final Color PANEL_DARK = new Color(34, 40, 52);
    public static final Color CARD_DARK = new Color(44, 52, 68);
    public static final Color CARD_HOVER = new Color(56, 66, 86);
    public static final Color ACCENT_GOLD = new Color(218, 165, 32);
    public static final Color ACCENT_BLUE = new Color(56, 189, 248);
    public static final Color TEXT_LIGHT = new Color(241, 245, 249);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);

    public static final String[] PLAYER_NAMES = {
        "1. Osztrák–Magyar Monarchia",
        "2. Német Császárság",
        "3. Antant (Francia–Brit)",
        "4. Orosz Birodalom"
    };

    public static final Color[] PLAYER_COLORS = {
        new Color(239, 68, 68),   // Piros
        new Color(59, 130, 246),  // Kék
        new Color(16, 185, 129),  // Zöld
        new Color(245, 158, 11)   // Sárga/Narancs
    };

    // 11. évfolyamos történelem tankönyv alapján összeállított 24 mezős pálya
    public static List<Tile> createTiles() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(0, "START: 1914", "A nagy háború kitörése", TileType.START, null));
        tiles.add(new Tile(1, "Szarajevó", "1914. jún. 28. Merénylet Ferenc Ferdinánd ellen.", TileType.QUIZ,
                new Question("Ki követte el a szarajevói merényletet?",
                        new String[]{"Gavrilo Princip", "Franz Conrad", "Alekszandr Kerenszkij", "Tisza István"}, 0)));
        tiles.add(new Tile(2, "Hadüzenet", "1914. júl. 28. Az OMM hadat üzen Szerbiának.", TileType.EVENT_FORWARD, null));
        tiles.add(new Tile(3, "Schlieffen-terv", "Villámháborús terv két front ellen.", TileType.QUIZ,
                new Question("Melyik semleges ország területét sértette meg Németország a terv végrehajtásakor?",
                        new String[]{"Svájc", "Belgium", "Hollandia", "Dánia"}, 1)));
        tiles.add(new Tile(4, "Marne-i csata", "1914. szept. Megakad a német előrenyomulás.", TileType.EVENT_BACK, null));
        tiles.add(new Tile(5, "Állóháború", "Lövéshárkok rendszere a nyugati fronton.", TileType.QUIZ,
                new Question("Mi jellemezte leginkább a nyugati frontot 1915-től kezdve?",
                        new String[]{"Gyors villámháború", "Állóháború és kimerítő anyagcsata", "Kizárólag tengeri ütközetek", "Lovassági áttörések"}, 1)));
        tiles.add(new Tile(6, "Ypres (1915)", "Vegyi fegyverek (klórgáz) első bevetése.", TileType.EVENT_BACK, null));
        tiles.add(new Tile(7, "Gorlicei áttörés", "1915. május: Központi hatalmak sikere keleten.", TileType.EVENT_FORWARD, null));
        tiles.add(new Tile(8, "Olasz front", "1915: Olaszország belép az Antant oldalán.", TileType.QUIZ,
                new Question("Melyik titkos egyezményben ígértek területeket Olaszországnak az Antant hatalmak?",
                        new String[]{"Londoni egyezmény", "Breszt-litovszki béke", "Rapallói egyezmény", "Compiègne-i egyezmény"}, 0)));
        tiles.add(new Tile(9, "Isonzó és Doberdó", "Véres csaták a Karszt-fennsíkon.", TileType.QUIZ,
                new Question("Hány isonzói csata zajlott összesen az olasz fronton?",
                        new String[]{"4", "8", "12", "15"}, 2)));
        tiles.add(new Tile(10, "Verdun (1916)", "A 'verduni vérszivattyú' – hónapokig tartó ostrom.", TileType.EVENT_BACK, null));
        tiles.add(new Tile(11, "Somme-i csata", "Első alkalommal vetnek be harckocsikat (tankokat).", TileType.QUIZ,
                new Question("Melyik hadsereg vetett be először harckocsikat a Somme-i csatában?",
                        new String[]{"Német", "Brit", "Orosz", "Francia"}, 1)));
        tiles.add(new Tile(12, "Bruszilov-offenzíva", "1916. nyár: Orosz áttörés a keleti fronton.", TileType.EVENT_FORWARD, null));
        tiles.add(new Tile(13, "Jütlandi csata", "A háború legnagyobb tengeri ütközete.", TileType.QUIZ,
                new Question("Kik csaptak össze a jütlandi tengeri csatában?",
                        new String[]{"Brit és Német flotta", "Osztrák és Olasz flotta", "USA és Német flotta", "Francia és Orosz flotta"}, 0)));
        tiles.add(new Tile(14, "IV. Károly", "1916. nov.: Ferenc József halála után új uralkodó.", TileType.QUIZ,
                new Question("Milyen politikai lépéssel próbálkozott IV. Károly uralkodása alatt a béke érdekében?",
                        new String[]{"Korlátlan tengeralattjáró-háború", "Különbéke-tapogatózások", "Támadás Svájc ellen", "Azonnali kapituláció"}, 1)));
        tiles.add(new Tile(15, "Tengeralattjáró-harc", "Németország meghirdeti a korlátlan tengeralattjáró-háborút.", TileType.EVENT_BACK, null));
        tiles.add(new Tile(16, "USA hadba lépése", "1917. április: Az Egyesült Államok belép a háborúba.", TileType.EVENT_FORWARD, null));
        tiles.add(new Tile(17, "Orosz forradalmak", "1917: A cári rendszer bukása, bolsevik hatalomátvétel.", TileType.QUIZ,
                new Question("Ki vezette a bolsevikokat az 1917 októberi fordulat során?",
                        new String[]{"Vlagyimir Iljics Lenin", "II. Miklós cár", "Lev Trockij", "Alekszandr Kerenszkij"}, 0)));
        tiles.add(new Tile(18, "Caporetto (1917)", "A Monarchia és Németország nagy áttörése Olaszországban.", TileType.EVENT_FORWARD, null));
        tiles.add(new Tile(19, "Wilsoni 14 pont", "1918. jan.: Az amerikai elnök béketervezete.", TileType.QUIZ,
                new Question("Mit hangsúlyozott ki Woodrow Wilson elnök 14 pontja a nemzetiségekkel kapcsolatban?",
                        new String[]{"Gyarmatosítás növelése", "Önrendelkezési jog", "Abszolutizmus fenntartása", "Kényszerű asszimiláció"}, 1)));
        tiles.add(new Tile(20, "Breszt-litovszki béke", "1918. márc.: Szovjet-Oroszország kilép a háborúból.", TileType.QUIZ,
                new Question("Milyen következménnyel járt a breszt-litovszki béke Oroszországra nézve?",
                        new String[]{"Hatalmas területi veszteségeket szenvedett", "Új gyarmatokat szerzett", "Területeket kapott Németországtól", "Nem változtak a határai"}, 0)));
        tiles.add(new Tile(21, "Katonai összeomlás", "1918 ősz: A központi hatalmak tartalékai kimerülnek.", TileType.EVENT_BACK, null));
        tiles.add(new Tile(22, "Padovai fegyverszünet", "1918. nov. 3.: Az Osztrák–Magyar Monarchia fegyverszünete.", TileType.QUIZ,
                new Question("Mikor írta alá Németország a compiègne-i fegyverszünetet?",
                        new String[]{"1918. november 11.", "1919. június 28.", "1920. június 4.", "1917. október 25."}, 0)));
        tiles.add(new Tile(23, "CÉL: 1918. NOV. 11.", "A fegyverek elhallgatnak – A háború vége!", TileType.FINISH, null));
        return tiles;
    }
}
