package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Calculator;
import xxl.exceptions.FileNotSavedException;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {

        try {
            _receiver.isValidSpreadsheet();
        }
        catch (FileNotSavedException e) {
            if (Form.confirm(Prompt.saveBeforeExit())) {
                DoSave saveBeforeExit = new DoSave(_receiver);
                saveBeforeExit.execute();
            }
        }
        
        int lines = Form.requestInteger(Prompt.lines());
        int columns = Form.requestInteger(Prompt.columns());
        _receiver.newSpreadsheet(lines, columns);
    }
    
}
