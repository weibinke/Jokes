package com.kb.jokes.data.storage;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.MLog;

public class DbAdpterImp implements IDbAdapter {
	private static final String TAG = "DbAdpterImp";
	
	@Override
	public List<JokeInfo> getJokesList(int type, int maxCount) {
		long start = System.currentTimeMillis();
		List<JokeInfo> result = queryJokeList(new Select().from(JokeInfo.class).where("Type=?",type).orderBy("UpdateTime desc").limit(maxCount));
		
		MLog.i(TAG, "getJokesList end ,type=" + type + ",maxCount=" + maxCount + ",resultCount=" + result.size() + ",cost=" + (System.currentTimeMillis() - start));
		return result;
	}
	
	@Override
	public List<JokeInfo> getMyFavoriteList() {
		long start = System.currentTimeMillis();
		
		List<JokeInfo> result = queryJokeList(new Select().from(JokeInfo.class).where("IsFavorite=?",true).orderBy("UpdateTime desc"));
		
		MLog.i(TAG, "getMyFavoriteList end ,resultCount=" + result.size() + ",cost=" + (System.currentTimeMillis() - start));
		return result;
	}
	
	@Override
	public List<JokeInfo> getMyThumbupList() {
		long start = System.currentTimeMillis();
		
		List<JokeInfo> result = queryJokeList(new Select().from(JokeInfo.class).where("IsThumbup=?",true).orderBy("UpdateTime desc"));
		
		MLog.i(TAG, "getMyThumbupList end ,resultCount=" + result.size() + ",cost=" + (System.currentTimeMillis() - start));
		return result;
	}
	
	public List<JokeInfo> queryJokeList(From from) {
		List<JokeInfo> result = new ArrayList<JokeInfo>();
		List<JokeInfo> temp = from.execute();
		//db��������л����߼�����ñȽ϶��ģ����ﻹ��cloneһ�ݳ�ȥ����
		JokeInfo item = null;
		for (JokeInfo jokeInfo : temp) {
			item = new JokeInfo();
			item.updateField(jokeInfo);
			result.add(item);
		}
		return result;
	}

	@Override
	public void saveJokes(List<JokeInfo> jokeInfos) {
		long start = System.currentTimeMillis();
		if(jokeInfos == null || jokeInfos.size() == 0){
			MLog.e(TAG, "saveJokes jokes is empty");
			
			return;
		}
		
		MLog.i(TAG, "saveJokes start.size=" + jokeInfos.size());
//		// ����
//		Collections.sort(jokeInfos, new Comparator<JokeInfo>() {
//			public int compare(JokeInfo lhs, JokeInfo rhs) {
//				return (int) (rhs.getJokeId() - lhs.getJokeId());
//			}
//		});

		ActiveAndroid.beginTransaction();
		try {
			JokeInfo item = null;
			for (int i = 0; i < jokeInfos.size(); i++) {
				item = jokeInfos.get(i);
				updateJoke(item);
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		
		MLog.i(TAG, "saveJokes end.size=" + jokeInfos.size() + ",cost=" + (System.currentTimeMillis() - start));
	}
	
	@Override
	public void updateJoke(final JokeInfo item) {
		if(item == null || !JokeInfo.isJokeIdVailed(item.getJokeId())){
			MLog.e(TAG, "saveJoke jokeid is invailed,jokeid=" + item.getJokeId());
			return;
		}
		
		if(item.getId() != null){
			//������item��id��Ϊnull��˵����dbֱ�ӹ�������ģ�ֱ��save���У�db����л����߼���������ȥget����Ȼget֮������ݱ���ԭ
			item.save();
			MLog.i(TAG, "saveJokes id is not null,update it jokeid=" + item.getJokeId() + ",id=" + item.getId());
			return;
		}
		
		JokeInfo oldItem = getJokeInfoByJokeId(item.getJokeId());
		if(oldItem != null){
			MLog.i(TAG, "saveJokes item already exist,update it jokeid=" + item.getJokeId() + ",id=" + item.getId());
			oldItem.updateField(item);
			oldItem.save();
		}else{
			item.save();
			MLog.e(TAG, "saveJoke item not exist,jokeid=" + item.getJokeId());
		}
	}
	
	/**
	 * ����jokeid��ȡJokeInfo����ע��db��������л����߼������executeSingle֮�󽫿��ܸı��ڴ��е����ݣ������пӣ�ҪС��
	 */
	@Override
	public JokeInfo getJokeInfoByJokeId(long jokeId){
		return new Select().from(JokeInfo.class).where("JokeId=?",jokeId).executeSingle();
	}

	@Override
	public long getStartPos(int type) {
		JokeInfo item = new Select().from(JokeInfo.class).where("Type=?",type).orderBy("UpdateTime desc").limit(1).executeSingle();
		if(item != null){
			return item.getJokeId();
		}
		
		return 0;
	}

	@Override
	public long getEndPos(int type) {
		JokeInfo item = new Select().from(JokeInfo.class).where("Type=?",type).orderBy("UpdateTime").limit(1).executeSingle();
		if(item != null){
			return item.getJokeId();
		}
		
		return 0;
	}

}
