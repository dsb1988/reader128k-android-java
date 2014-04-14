package net.reader128k.domain;

public final class Pair<T1, T2> {
	public final T1 _1;
	public final T2 _2;

	public Pair(T1 _1, T2 _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public static <T1, T2> Pair<T1, T2> of(T1 _1, T2 _2) {
		return new Pair<T1, T2>(_1, _2);
	}
}
