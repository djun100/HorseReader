package com.reader.code.epub;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.reader.book.AreaDraw;
import com.reader.book.Book;
import com.reader.book.Page;
import com.reader.book.model.MarkupElement;

public class Epub extends Book {

	public Epub(File file) {
		this.bookFile = file;
	}

	@Override
	public void openBook() {
		// this.bookFile;
		try {
			ZipFile epub = new ZipFile(bookFile);
			Enumeration<? extends ZipEntry> enumer = epub.entries();
			while (enumer.hasMoreElements()) {
				ZipEntry zipentry = enumer.nextElement();
				// System.out.println(zipentry.getName());
				if (zipentry.getName().equals("META-INF/container.xml")) {
					ContainerHandler handler = new ContainerHandler(
							epub.getInputStream(zipentry));
					handler.handle();
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeBook() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContent(int start, ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pushIntoList(BlockingQueue<MarkupElement> elements,
			CopyOnWriteArrayList<Page> pages, LinkedList<AreaDraw> lines) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
