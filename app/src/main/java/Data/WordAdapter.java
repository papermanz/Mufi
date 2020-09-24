package Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minhhieu.mufi.LearnActivity;
import com.minhhieu.mufi.R;

import java.util.List;

public class WordAdapter extends BaseAdapter {

    private LearnActivity context;
    private int layout;
    private List<Word> wordList;

    public WordAdapter(LearnActivity context, int layout, List<Word> wordList) {
        this.context = context;
        this.layout = layout;
        this.wordList = wordList;
    }

    @Override
    public int getCount() {
        return wordList.size(); // số dòng muốn listview hiển thị
    }

    @Override
    public Word getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtWordE;


    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if(view==null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtWordE = (TextView) view.findViewById(R.id.tvword1);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final Word word1 = wordList.get(position);

        holder.txtWordE.setText(word1.getEng());


        // bắt sự kiện xoá

        holder.txtWordE.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.DialogDeleteWord(word1.getEng(),word1.getIdWord());
                return false;
            }
        });

        return view;
    }
}
