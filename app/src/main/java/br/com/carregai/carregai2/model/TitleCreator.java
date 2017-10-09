package br.com.carregai.carregai2.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {

    public static TitleCreator mTitleCreator;
    private List<TitleParent> mTitleParent;

    public TitleCreator(Context context) {
        this.mTitleParent = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            TitleParent title = new TitleParent(String.format("Caller %d", i));
            mTitleParent.add(title);
        }
    }

    public static TitleCreator get(Context context){
        if(mTitleCreator == null){
            mTitleCreator = new TitleCreator(context);
        }
        return mTitleCreator;
    }

    public List<TitleParent> getAll() {
        return mTitleParent;
    }
}
