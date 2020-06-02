package com.example.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.listview.R;
import com.example.listview.model.Evento;

import java.util.ArrayList;

public class EventosAdapter extends ArrayAdapter<Evento> {

    public EventosAdapter(Context context, ArrayList<Evento> eventos) {

        super(context, 0, eventos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Evento evento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_eventos, parent, false);
        }
        // Lookup view for data population
        TextView textViewNomeEvento = (TextView) convertView.findViewById(R.id.textViewNomeEvento);
        TextView textViewLocalEvento = (TextView) convertView.findViewById(R.id.textViewLocalEvento);
        TextView editTextDataEvento = (TextView) convertView.findViewById(R.id.textViewDataEvento);
        // Populate the data into the template view using the data object
        textViewNomeEvento.setText(evento.getNomeDoEvento());
        textViewLocalEvento.setText(evento.getLocalDoEvento());
        editTextDataEvento.setText(evento.getDataDoEvento());
        // Return the completed view to render on screen
        return convertView;


    }
}
