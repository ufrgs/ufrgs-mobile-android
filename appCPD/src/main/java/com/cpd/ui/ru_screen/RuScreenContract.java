package com.cpd.ui.ru_screen;

import com.cpd.network.models.Ru;

import java.util.List;

/**
 * Created by Theo on 02/06/17.
 */

public interface RuScreenContract {
    interface View {
        void showRuInfo(List<Ru> list);
        void showMessage(String msg);
        void showFailMessage();
    }

    interface Presenter {
        void getRuInformation();
    }
}
