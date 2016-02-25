package teacherToolBox;

import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class CheckBoxCellFactory implements Callback
{
    @Override
    public TableCell call(Object param)
    {
        return new CheckBoxTableCell();
    }
}
