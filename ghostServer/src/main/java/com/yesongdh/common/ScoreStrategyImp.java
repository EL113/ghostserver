package com.yesongdh.common;

import com.yesongdh.bean.StoryStat;

public class ScoreStrategyImp implements ScoreStrategy {

	@Override
	public int calScore(StoryStat storyStat) {
		return storyStat.getThumbUp() * 5 - storyStat.getThumbDown() * 3 + storyStat.getCollection() * 2;
	}

}
