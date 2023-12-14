package com.jnu.student.data;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.student.MainActivity;
import com.jnu.student.R;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DataBankTest {
    DataBank dataSaverBackup;
    ArrayList<BookItem> bookItemsBackup;


    @Before
    public void setUp() throws Exception {
        dataSaverBackup = new DataBank();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        bookItemsBackup = dataSaverBackup.LoadBookItems(targetContext);
        dataSaverBackup.SaveBookItems(targetContext, new ArrayList<>());
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dataSaverBackup.SaveBookItems(targetContext, bookItemsBackup);
    }

    public void testLoadBookItem() {
        DataBank dataBank = new DataBank();
        ArrayList<BookItem> bookItems = dataBank.LoadBookItems(
                InstrumentationRegistry.getInstrumentation().getTargetContext()
        );
        Assert.assertEquals(0, bookItems.size());
    }
    @Test
    public void testSaveBookItem() {
        DataBank dataSaver = new DataBank();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<BookItem> bookItems = new ArrayList<>();
        BookItem bookItem = new BookItem("测试1", R.drawable.book_1);
        bookItems.add(bookItem);
        bookItem = new BookItem("测试2", R.drawable.book_2);
        bookItems.add(bookItem);

        dataSaver.SaveBookItems(targetContext, bookItems);

        DataBank dataLoader = new DataBank();
        ArrayList<BookItem> bookItemsRead = dataLoader.LoadBookItems(targetContext);
        assertNotSame(bookItems, bookItemsRead);
        assertEquals(bookItems.size(), bookItemsRead.size());
        for (int index = 0; index < bookItems.size(); ++index) {
            assertNotSame(bookItems.get(index), bookItemsRead.get(index));
            assertEquals(bookItems.get(index).getName(), bookItemsRead.get(index).getName());
            assertEquals(bookItems.get(index).getImageId(), bookItemsRead.get(index).getImageId());
        }

    }
}