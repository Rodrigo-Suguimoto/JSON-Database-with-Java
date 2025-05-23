import java.sql.Array;

class Army {

    public static void createArmy() {
        int UNITS_QTD = 5;
        for (int i = 0; i < UNITS_QTD; i++) {
            String unitName = "Unit " + i + 1;
            new Unit(unitName);
        }

        String[] Knights = new String[]{"Sir Ned", "Sir Lawrence", "Sir Hightower"};
        for (String knight : Knights) {
            new Knight(knight);
        }

        General general = new General("Sir General");
        Doctor doctor = new Doctor("Doctor Who");
    }


    // Don't change the code below
    static class Unit {
        static String nameUnit;
        static int countUnit;

        public Unit(String name) {
            countUnit++;
            nameUnit = name;

        }
    }

    static class Knight {
        static String nameKnight;
        static int countKnight;

        public Knight(String name) {
            countKnight++;
            nameKnight = name;

        }
    }

    static class General {
        static String nameGeneral;
        static int countGeneral;

        public General(String name) {
            countGeneral++;
            nameGeneral = name;

        }
    }

    static class Doctor {
        static String nameDoctor;
        static int countDoctor;

        public Doctor(String name) {
            countDoctor++;
            nameDoctor = name;

        }
    }

    public static void main(String[] args) {
        createArmy();
        System.out.println(Unit.countUnit);
        System.out.println(Knight.countKnight);
        System.out.println(General.countGeneral);
        System.out.println(Doctor.countDoctor);
    }

}