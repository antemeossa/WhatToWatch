package com.example.movie_tinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class helpDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("HELP:")
                .setMessage("-IMPORTANT: This is a still developing app so it does not work smoothly as the others we advise you to follow the following steps:\n" +
                        "\n" +
                        "        -In order to play you have to invite a friend by his exact username or you can select from your friends list.\n" +
                        "        -There are no invite acception option so you have to invite eachother seperately and play your game.\n" +
                        "        -You will be able to see the list after you are both finished so be sure both of you have finished the list.\n" +
                        "        -You will know the list that we gave you ended after the like or dislike buttons dissappear and a button on middle appears.\n" +
                        "        -Other than theese the app is pretty self explained so have fun :)\n" +
                        "        -KNOWN BUGS:\n" +
                        "            -Sometimes one players list doesnt appear because of the database, not that there is anything wrong with it but the data doesnt gets to the app.")
                .setPositiveButton("GOT IT!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return  builder.create();
    }
}
