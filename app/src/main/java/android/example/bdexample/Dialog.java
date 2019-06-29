package android.example.bdexample;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    private EditText edId;
    private DialogIdListener listener;//интерфейс
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.id_dialog, null);
        edId = view.findViewById(R.id.inputId);
        builder.setView(view)
        .setTitle("Dialog")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    Integer id = Integer.parseInt(edId.getText().toString());
                    listener.applyId(id);
                }catch (NumberFormatException e){
                    Toast.makeText(getActivity(), "Wrong id", Toast.LENGTH_SHORT).show();//всплывающее окно с текстом
                    System.out.println("Error");
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogIdListener) context;//объявляем интерфейс
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogIdListener");
        }
    }

    public interface DialogIdListener{
        void applyId(Integer id);//этот метод потом вызываем в MainActivity для работы с id
    }
}
