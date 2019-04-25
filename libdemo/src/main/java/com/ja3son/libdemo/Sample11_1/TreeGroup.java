package com.ja3son.libdemo.Sample11_1;

import java.util.ArrayList;
import java.util.List;

import static com.ja3son.libdemo.Sample11_1.MySurfaceView.UNIT_SIZE;

public class TreeGroup {
    TreeForDraw tfd;
    List<SingleTree> alist = new ArrayList<SingleTree>();

    public TreeGroup(MySurfaceView mv) {
        tfd = new TreeForDraw(mv);
        alist.add(new SingleTree(0, 0, 0, this));
        alist.add(new SingleTree(8 * UNIT_SIZE, 0, 0, this));
        alist.add(new SingleTree(5.7f * UNIT_SIZE, 5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(0, -8 * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(-5.7f * UNIT_SIZE, 5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(-8 * UNIT_SIZE, 0, 0, this));
        alist.add(new SingleTree(-5.7f * UNIT_SIZE, -5.7f * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(0, 8 * UNIT_SIZE, 0, this));
        alist.add(new SingleTree(5.7f * UNIT_SIZE, -5.7f * UNIT_SIZE, 0, this));
    }

    public void calculateBillboardDirection() {
        for (int i = 0; i < alist.size(); i++) {
            alist.get(i).calculateBillboardDirection();
        }
    }

    public void drawSelf(int texId) {
        for (int i = 0; i < alist.size(); i++) {
            alist.get(i).drawSelf(texId);
        }
    }
}