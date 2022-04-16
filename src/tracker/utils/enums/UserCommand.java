package tracker.utils.enums;

public enum UserCommand {
   ADD_STUDENT_COMMAND("add students"),
    ADD_POINTS_COMMAND("add points"),
    BACK_COMMAND("back"),
    EXIT_COMMAND("exit"),
    LIST_COMMAND("list"),
    FIND_COMMAND("find"),
    STATISTICS("statistics"),
    SETUP_TEST_DATA_COMMAND("setup test"),
    NO_COMMAND("");

   private String commandText;

   UserCommand(String commandText) {
       this.commandText = commandText;
   }

   public String getCommandText() {
       return this.commandText;
   }
}
