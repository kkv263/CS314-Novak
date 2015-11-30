// graphtest.java      GSN      
// 25 Mar 09; 27 Mar 09; 30 Mar 09; 03 Apr 09; 28 Aug 09; 14 Nov 09
// 16 Apr 10

public class graphtest
{
    public static Graph texas = new Graph( Cons.readlist( Cons.list(
"(abilene   32.449  -99.732 ((roscoe 47) (haskell 54) (san-angelo 88) (brownwood 85) (fort-worth 158)))",
"(alice   27.752  -98.068 ((corpus-christi 39)(laredo 94)))",
"(amarillo   35.222 -101.830 ((dumas 47) (plainview 76) (hereford 43) (pampa 54) (childress 116)))",
"(austin   30.267  -97.742 ((san-antonio 76) (temple 62) (bryan 86) (lampasas 61) (junction 122)))",
"(bay-city   28.983  -95.968 ((houston 88)))",
"(beaumont   30.086  -94.101 ((port-arthur 18)))",
"(beeville   28.401  -97.747 ((corpus-christi 60) (san-antonio 93)))",
"(big-spring   32.250 -101.477 ((roscoe 59) (san-angelo 86)))",
"(brownsville   25.901  -97.496 ())",
"(brownwood   31.709  -98.990 ((lampasas 71) (san-angelo 96)))",
"(bryan   30.674  -96.369 ())",
"(childress   34.426 -100.203 ())",
"(columbus   29.706  -96.538 ((austin 88) (san-antonio 119)))",
"(corpus-christi   27.800  -97.395 ((harlingen 107)))",
"(corsicana   32.095  -96.468 ((huntsville 117) (tyler 71) (waco 55)))",
"(dallas   32.794  -96.799 ((corsicana 57) (tyler 96) (denton 39) (hillsboro 58)))",
"(del-rio   29.363 -100.895 ((sonora 90) (san-antonio 151) (van-horn 304)))",
"(denton   33.215  -97.132 ((fort-worth 30)))",
"(dumas   35.866 -101.972 ())",
"(eagle-pass   28.709 -100.498 ((del-rio 56) (laredo 126)))",
"(el-paso   31.759 -106.485 ())",
"(fort-worth   32.725  -97.320 ((dallas 31) (hillsboro 51)))",
"(galveston   29.301  -94.797 ())",
"(greenville   33.138  -96.110 ((dallas 47) (sherman 52) (tyler 79)))",
"(harlingen   26.190  -97.695 ((mcallen 31) (brownsville 24)))",
"(haskell   33.158  -99.743 ((wichita-falls 95)))",
"(hereford   34.815 -102.396 ((muleshoe 45)))",
"(hillsboro   32.011  -97.129 ())",
"(houston   29.763  -95.362 ((galveston 47) (beaumont 85) (victoria 124) (columbus 74) (huntsville 70) (bryan 99)))",
"(huntsville   30.723  -95.550 ())",
"(junction   30.489  -99.771 ((san-antonio 116)))",
"(killeen   31.117  -97.727 ((temple 30) (lampasas 28)))",
"(lamesa   32.737 -101.950 ((midland 52) (big-spring 44)))",
"(lampasas   31.064  -98.180 ())",
"(laredo   27.506  -99.506 ())",
"(longview   32.501  -94.739 ())",
"(lubbock   33.578 -101.854 ((plainview 46) (muleshoe 70) (seminole 79) (lamesa 64) (snyder 88)))",
"(lufkin   31.338  -94.728 ((nacogdoches 19) (houston 123) (beaumont 108)))",
"(mcallen   26.203  -98.229 ((brownsville 55) (laredo 146)))",
"(midland   31.997 -102.077 ((big-spring 39)))",
"(mineral-wells   32.808  -98.112 ((fort-worth 60) (abilene 130) (wichita-falls 89)))",
"(muleshoe   34.226 -102.722 ())",
"(nacogdoches   31.603  -94.654 ((longview 80)))",
"(odessa   31.846 -102.366 ((midland 20) (pecos 76)))",
"(palestine   31.762  -95.630 ((corsicana 54) (tyler 50) (nacogdoches 56)))",
"(pampa   35.536 -100.958 ())",
"(pecos   31.423 -103.492 ((van-horn 88)))",
"(plainview   34.185 -101.705 ((vernon 159)))",
"(port-arthur   29.899  -93.928 ())",
"(roscoe   32.446 -100.537 ())",
"(san-angelo   31.464 -100.436 ())",
"(san-antonio   29.424  -98.492 ((laredo 154)))",
"(seminole   32.719 -102.643 ((odessa 64) (lamesa 40)))",
"(sherman   33.636  -96.608 ((dallas 65) (wichita-falls 113)))",
"(snyder   32.718 -100.916 ((lamesa 67) (roscoe 30)))",
"(sonora   30.578 -100.642 ((junction 59) (san-angelo 68)))",
"(temple   31.098  -97.342 ((waco 37)))",
"(tyler   32.351  -95.300 ((longview 34) (lufkin 86)))",
"(van-horn   31.040 -104.829 ((el-paso 156) (sonora 254)))",
"(vernon   34.154  -99.264 ((childress 68)))",
"(victoria   28.805  -97.002 ((corpus-christi 78) (beeville 55)))",
"(waco   31.549  -97.145 ((hillsboro 32) (bryan 87)))",
"(wichita-falls   33.914  -98.492 ((vernon 42) (fort-worth 106)))"
)));

    public static void dtest(String start, String goal) {
        System.out.println("start Dijkstra");
        int dijknodes = texas.dijkstra(texas.vertex(start));
        System.out.println("end Dijkstra, nodes = " + dijknodes);
        System.out.println("Road Distance " + start + " to " + goal
                           + " = " + texas.vertex(goal).cost());
        Cons route = texas.pathto(goal);
        Cons.printanswer("Route to " + goal + " = ", route);
        System.out.println(); }

    public static void atest(String start, String goal, String hname,
                             Heuristic h) {
        System.out.println("start A* " + hname);
        int nodes = texas.astar(texas.vertex(start), texas.vertex(goal), h);
        System.out.println("end A*, nodes = " + nodes);
        System.out.println("Road Distance " + start + " to " + goal
                           + " = " + texas.vertex(goal).cost());
        Cons route = texas.pathto(goal);
        Cons.printanswer("Route to " + goal + " = ", route);
        System.out.println(); }

    public static void main( String[] args ) {
        System.out.println("start");
        System.out.println("Great-Circle Distance Austin to Muleshoe = " +
           texas.vertex("austin").distanceTo(texas.vertex("muleshoe")) );
        System.out.println();

        dtest("austin", "muleshoe");
        System.out.println();
        System.out.println("Graph after running Dijkstra:");
        texas.print();
        System.out.println();
        dtest("laredo", "haskell");
        dtest("dumas", "corsicana");

        atest("austin", "muleshoe", "dist", Graph.dist);
        atest("laredo", "haskell", "dist", Graph.dist);
        atest("dumas", "corsicana", "dist", Graph.dist);

        atest("austin", "muleshoe", "halfass", Graph.halfass);
        atest("laredo", "haskell", "halfass", Graph.halfass);
        atest("dumas", "corsicana", "halfass", Graph.halfass);

        atest("austin", "muleshoe", "zip", Graph.zip);
        atest("laredo", "haskell", "zip", Graph.zip);
        atest("dumas", "corsicana", "zip", Graph.zip);

        atest("austin", "muleshoe", "randombs", Graph.randombs);
        atest("laredo", "haskell", "randombs", Graph.randombs);
        atest("dumas", "corsicana", "randombs", Graph.randombs);

        atest("austin", "muleshoe", "randomlies", Graph.randomlies);
        atest("laredo", "haskell", "randomlies", Graph.randomlies);
        atest("dumas", "corsicana", "randomlies", Graph.randomlies);

        System.out.println("start Prim");
        int spancost = texas.prim(texas.vertex("austin"));
        System.out.println("end Prim, total cost = " + spancost);

        System.out.println("edgecost austin to temple = " +
                           texas.edgecost(texas.vertex("austin"),
                                          texas.vertex("temple")) );
        Cons routem = texas.pathto("muleshoe");
        Cons.printanswer("Route Austin to Muleshoe = ", routem);

        System.out.println("pathcost austin to muleshoe = " +
                           texas.pathcost(texas.vertex("muleshoe")) );
        System.out.println("Total cost of all roads = " +
                           texas.totalcost() );
        System.out.println();
        System.out.println("Graph after running Prim:");
        texas.print();

    }
}
