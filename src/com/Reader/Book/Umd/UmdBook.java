package com.Reader.Book.Umd;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.Reader.Book.Book;
import com.Reader.Book.CharInfo;
import com.Reader.Book.BookBuffer;
public class UmdBook implements Book {
	protected UmdParse umdStream;
	public UmdInfo umdInfo = null;
	private File umdFile;
	UmdInflate umdinflate;
	private BookBuffer bookBuffer = new BookBuffer(this);
	public UmdBook(File umd) throws IOException {
		umdFile = umd;
		umdInfo = new UmdInfo(umd);
		umdInfo.parseBook();
		umdinflate = new UmdInflate(this);
	}

	public File getFile() {
		return umdFile;
	}

	public int getContent(int start,ByteBuffer contentBuffer)  throws IOException{
		int length = contentBuffer.capacity();
		byte[] content;
		if (this.getPointerInWhichBlock(start) == this
				.getPointerInWhichBlock(start + length - 1)) {
			content = umdinflate.getContentBlock(this
					.getPointerInWhichBlock(start), this
					.getPointerInBlockLocal(start), this
					.getPointerInBlockLocal(length));
			contentBuffer.put(content);
		} else {
			content = umdinflate.getContentBlock(this
					.getPointerInWhichBlock(start), this
					.getPointerInBlockLocal(start), UmdParse.BLOCKSIZE);
			if (content == null) {
				System.out.println(" Is null\t"
						+ this.getPointerInWhichBlock(start) + "\t"
						+ this.getPointerInBlockLocal(start) + "\t"
						+ UmdParse.BLOCKSIZE);
			}
			contentBuffer.put(content);
			for (int i = this.getPointerInWhichBlock(start) + 1; i < this
					.getPointerInWhichBlock(start + length - 1); i++) {
				content = umdinflate.getContentBlock(this
						.getPointerInWhichBlock(start), 0, UmdParse.BLOCKSIZE);
				contentBuffer.put(content);
			}
			content = umdinflate.getContentBlock(this
					.getPointerInWhichBlock(start + length - 1), this
					.getPointerInBlockLocal(0), (start + length - 1)
					% UmdParse.BLOCKSIZE);
			contentBuffer.put(content);
		}

		ByteOrder order = ByteOrder.LITTLE_ENDIAN;
		contentBuffer.order(order);
		return start + length - 1;
	}
	
	public int getPointerInWhichBlock(int pointer) {
		return pointer / (UmdParse.BLOCKSIZE);
	}

	public int getPointerInBlockLocal(int pointer) {
		return pointer % (UmdParse.BLOCKSIZE);
	}

	public int getChapterLocal(int num) {
		if (num < 0)
			return -1;
		return (int) this.umdInfo.chapterList.get(num).chapterStartLocal;
	}

	public int localIsInWhichChapter(int local) {
		int chapterNum = 0;
		for (; chapterNum < umdInfo.chapterList.size(); chapterNum++) {
			if (local < umdInfo.chapterList.get(chapterNum).chapterStartLocal) {
				return chapterNum--;
			}
		}
		return -1;
	}

	public byte[] getBlockContent(int index) throws IOException {
		UmdInflate umdinflate = new UmdInflate(this);
		byte[] uncomp = umdinflate.Inflate(getBlockData(index));
		Byte b = new Byte(uncomp[0]);
		System.out.println("first byte:\t " + b.toString());
		BytesTransfer.byteAlign(uncomp);
		return uncomp;
	}
	public int blockIndex = -1;
	public byte[] blockDataBuffer = null;
	public byte[] getBlockData(int index) throws IOException {
		if (index == blockIndex){
			return blockDataBuffer;
		}
		byte bytes[] = null;
		try {
			umdStream = new UmdParse(this.umdFile, "r");
			UmdInfo.Block b = this.umdInfo.getBlock(index);
			this.umdStream.seek(b.filePointer);
			bytes = new byte[b.blockSize];
			umdStream.read(bytes);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			umdStream.close();
			umdStream = null;
		}
		blockDataBuffer = bytes;
		blockIndex = index;
		return bytes;
	}

	public class Block {
		protected int blockNo;
		protected long filePointer;
		protected int blockSize;

		public Block(long filepointer, int size) {
			filePointer = filepointer;
			blockSize = size;
		}

		public void setBlockNo(int num) {
			blockNo = num;
		}

		public long getPointer() {
			return filePointer;
		}
	}


	public void openBook() {

		try {
			this.umdStream = new UmdParse(this.umdFile, "r");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void closeBook() {
		if (umdStream != null) {
			try {
				umdStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void excuteCmd(int cmd) {

	}

	public int size() {
		// TODO Auto-generated method stub
		return this.umdInfo.getSize();

	}

	public CharInfo getChar(int mEnd) {
		// if(mEnd > boo)

		CharInfo charinfo = new CharInfo();
		charinfo.character = this.bookBuffer.getChar(mEnd);
		charinfo.length = 2;
		charinfo.position = mEnd;
		return charinfo;
		/*
		int size = umdInfo.getSize();

		if (mEnd >= size || mEnd < 0) {
			return null;
		}
		try {
			ByteBuffer buf = ByteBuffer.allocate(2);
			this.getContent(mEnd, buf);
			buf.flip();
			CharInfo charinfo = new CharInfo();
			charinfo.character = buf.getChar();
			charinfo.length = 2;
			charinfo.position = mEnd;
			return charinfo;
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//return null;
	}

	public CharInfo getPreChar(int start) {
		return getChar(start - 2);
	}
}
