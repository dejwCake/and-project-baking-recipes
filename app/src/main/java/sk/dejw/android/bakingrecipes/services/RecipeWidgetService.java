package sk.dejw.android.bakingrecipes.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import sk.dejw.android.bakingrecipes.GridRemoteViewsFactory;

public class RecipeWidgetService extends RemoteViewsService {

    public static final String TAG = RecipeWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}
