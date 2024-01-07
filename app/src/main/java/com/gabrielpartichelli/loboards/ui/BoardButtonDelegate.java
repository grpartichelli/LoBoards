package com.gabrielpartichelli.loboards.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class BoardButtonDelegate extends AccessibilityDelegateCompat {
    View after;
    public BoardButtonDelegate(View after) {
        this.after = after;
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull View host, @NonNull AccessibilityNodeInfoCompat info) {
        info.setTraversalAfter(this.after);
        super.onInitializeAccessibilityNodeInfo(host, info);
    }
}
