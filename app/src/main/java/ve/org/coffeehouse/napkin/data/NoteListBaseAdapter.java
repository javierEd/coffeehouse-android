package ve.org.coffeehouse.napkin.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ve.org.coffeehouse.napkin.R;

/**
 * Created by javier on 27/09/16.
 */

public class NoteListBaseAdapter extends BaseAdapter {

    private static ArrayList<Note> NoteArrayList;

    private LayoutInflater l_Inflater;

    public NoteListBaseAdapter(Context context, ArrayList<Note> results){
        NoteArrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return NoteArrayList.size();
    }

    public Object getItem(int position) {
        return NoteArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = l_Inflater.inflate(R.layout.note, parent, false);
        TextView hidden_id, content, created_at;
        hidden_id = (TextView) row.findViewById(R.id.hidden_id);
        content = (TextView) row.findViewById(R.id.content);
        created_at = (TextView) row.findViewById(R.id.created_at);
        hidden_id.setText(NoteArrayList.get(position).getId().toString());
        content.setText(NoteArrayList.get(position).getContent());
        created_at.setText(NoteArrayList.get(position).getCreated_at());

        return (row);
    }
}
