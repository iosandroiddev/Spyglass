/*
* Copyright 2015 LinkedIn Corp. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/

package com.linkedin.android.spyglass.ui;

import android.view.MotionEvent;

import com.linkedin.android.unittest.LinkedInRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * This is a series of tests for the MentionsEditText. It will use hard
 * implementations of it to test some of the functionality of the MentionsEditText.
 * Placing this class in the same package as the class we're testing so we can
 * call protected methods in the test.
 */
@Config(emulateSdk = 18)
@RunWith(LinkedInRobolectricRunner.class)
public class MentionsEditTextTest {

    private MentionsEditText mEditText;
    private RichEditorView mRichEditor;

    @Before
    public void setUp() throws Exception {
        mEditText = spy(new MentionsEditText(Robolectric.application));
        mEditText.setAvoidPrefixOnTap(true);
        mRichEditor = mock(RichEditorView.class);
        mEditText.setSuggestionsVisibilityManager(mRichEditor);
    }

    @Test
    public void testOnTouchEvent() throws Exception {
        MotionEvent event = mock(MotionEvent.class);
        doReturn(null).when(mEditText).getTouchedSpan(event);
        doReturn(true).when(mRichEditor).isDisplayingSuggestions();

        // Test that the MentionsEditText does not avoid "" as a prefix
        // Note: After typing "@", the keyword string is "", so avoiding "" would mean avoiding all
        // explicit mentions (keyword string is what the user typed minus explicit characters)
        doReturn("").when(mEditText).getCurrentKeywordsString();
        mEditText.onTouchEvent(event);
        verify(mEditText, never()).setAvoidedPrefix("");

        // Test that the MentionsEditText avoids a prefix as long as it has length > 0
        doReturn("a").when(mEditText).getCurrentKeywordsString();
        mEditText.onTouchEvent(event);
        verify(mEditText).setAvoidedPrefix("a");
    }

}