package jdk.mina.future.message;


import org.apache.mina.core.buffer.IoBuffer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2017/9/21
 */
public class HqFutureContractMess extends BaseFutureMessage {

	private List<HqFutureContract> hqFutureContracts;

	@Override
	public boolean readData(IoBuffer ioBuffer, int expectLength) {
		if (ioBuffer.hasRemaining()) {
			hqFutureContracts = new ArrayList<HqFutureContract>();
			getHqFutureContracts(ioBuffer, hqFutureContracts);
		}
		return true;
	}

	private void getHqFutureContracts(IoBuffer ioBuffer, List<HqFutureContract> hqFutureContracts) {
		int size = ioBuffer.getInt();
		for (int i = 0; i < size; i++) {
			byte[] bytes31 = new byte[31];
			byte[] bytes8 = new byte[8];
			try {
				HqFutureContract hqFutureContract = new HqFutureContract();
				ioBuffer.get(bytes31);
				hqFutureContract.setInstrumentId(new String(bytes31, "GBK").trim());
				ioBuffer.get(bytes31);
				hqFutureContract.setInstrumentName(new String(bytes31, "GBK").trim());
				ioBuffer.get(bytes31);
				hqFutureContract.setExchangeId(new String(bytes31, "GBK").trim());
				ioBuffer.get(bytes8);
				hqFutureContract.setOpenDate(new String(bytes8, "GBK"));
				ioBuffer.get(bytes8);
				hqFutureContract.setExpireDate(new String(bytes8, "GBK"));
				ioBuffer.get(bytes8);
				hqFutureContract.setProductId(new String(bytes8, "GBK").trim());

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IoBuffer writeData() throws Exception {
		String message="111";
		IoBuffer allocate = IoBuffer.allocate(1024);
		allocate.putInt(message.getBytes("gbk").length + 4);
		allocate.putInt(10004);
		allocate.put(message.getBytes("gbk"));
		allocate.flip();
		return allocate;
	}

	public List<HqFutureContract> getHqFutureContracts() {
		return hqFutureContracts;
	}

	public void setHqFutureContracts(List<HqFutureContract> hqFutureContracts) {
		this.hqFutureContracts = hqFutureContracts;
	}
}
