import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class JsonDatabaseTest extends StageTest<String> {
    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "ERROR";

    private static final String WRONG_EXIT = "The program should stop when 'exit' was entered.";
    private static final String WRONG_GET_EMPTY_CELL_WITH_ERROR = "When a user tries to get an empty cell from the database, you should print '" + ERROR_STATUS + "'.";
    private static final String WRONG_SET_VALUE_TO_CELL_WITH_OK = "When a user tries to set a value to a cell, you should save the value and print '" + OK_STATUS + "'.";
    private static final String WRONG_GET_VALUE = "When a user tries to get a non-empty cell, your program prints a wrong value.\nMaybe the problem is in processing 'set' action: If the specified cell already contains information, you should simply overwrite it with new value.";
    private static final String WRONG_DELETE = "When a user tries to delete a value from the specified cell, you should assign an empty string to this cell and print '" + OK_STATUS + "'.";
    private static final String WRONG_DELETE_EMPTY = "When a user tries to delete from a cell with empty value, you should just print '" + OK_STATUS + "'.";
    private static final String WRONG_DELETE_INDEX_OUT_OF_BOUNDS = "When a user tries to delete a cell whose index is out of bounds (less than 0 or greater than 1000), you should print '" + ERROR_STATUS + "'.";

    @DynamicTest(order = 1)
    CheckResult checkExit() {
        TestedProgram program = new TestedProgram("server");
        program.start();
        program.execute("exit");

        if (!program.isFinished()) {
            return CheckResult.wrong(WRONG_EXIT);
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult testExampleInputs() {
        TestedProgram program = new TestedProgram("server");
        program.start();

        String output;
        String expectedValue;

        output = program.execute("get 1");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR);
        }

        output = program.execute("set 1 Hello world!");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("set 1 HelloWorld!");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("get 1");
        expectedValue = "HelloWorld!";
        if (!output.contains(expectedValue)) {
            return CheckResult.wrong(WRONG_GET_VALUE +
                    "\n\nExpected:\n" + expectedValue + "\nYour output:\n" + output);
        }

        output = program.execute("delete 1");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE);
        }

        output = program.execute("delete 1");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("get 1");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR + "\nMaybe after deleting a cell, you didn't assign an empty string to it.");
        }

        output = program.execute("set 55 Some text here");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("get 55");
        expectedValue = "Some text here";
        if (!output.contains(expectedValue)) {
            return CheckResult.wrong(WRONG_GET_VALUE +
                    "\n\nExpected:\n" + expectedValue + "\nYour output:\n" + output);
        }

        output = program.execute("get 56");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR);
        }

        output = program.execute("delete 55");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE);
        }

        output = program.execute("delete 56");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("delete 100");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("delete 1001");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_INDEX_OUT_OF_BOUNDS);
        }

        program.execute("exit");

        return CheckResult.correct();
    }

    @DynamicTest(order = 3)
    CheckResult testCustomInputs() {
        TestedProgram program = new TestedProgram("server");
        program.start();

        String output;
        String expectedValue;

        output = program.execute("get 5");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR);
        }

        output = program.execute("set 5 Hyperskill is good!");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("set 5 Hyperskill is the best!");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("get 5");
        expectedValue = "Hyperskill is the best!";
        if (!output.contains(expectedValue)) {
            return CheckResult.wrong(WRONG_GET_VALUE +
                    "\n\nExpected:\n" + expectedValue + "\nYour output:\n" + output);
        }

        output = program.execute("delete 5");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE);
        }

        output = program.execute("delete 5");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("get 5");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR + "\nMaybe after deleting a cell, you didn't assign an empty string to it.");
        }

        output = program.execute("set 155 Sorabh Tomar");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_SET_VALUE_TO_CELL_WITH_OK);
        }

        output = program.execute("get 155");
        expectedValue = "Sorabh Tomar";
        if (!output.contains(expectedValue)) {
            return CheckResult.wrong(WRONG_GET_VALUE +
                    "\n\nExpected:\n" + expectedValue + "\nYour output:\n" + output);
        }

        output = program.execute("get 996");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_GET_EMPTY_CELL_WITH_ERROR);
        }

        output = program.execute("delete 155");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE);
        }

        output = program.execute("delete 996");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("delete 595");
        if (!output.toUpperCase().contains(OK_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_EMPTY);
        }

        output = program.execute("delete 0");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_INDEX_OUT_OF_BOUNDS);
        }

        output = program.execute("delete 1001");
        if (!output.toUpperCase().contains(ERROR_STATUS)) {
            return CheckResult.wrong(WRONG_DELETE_INDEX_OUT_OF_BOUNDS);
        }

        program.execute("exit");

        return CheckResult.correct();
    }
}
