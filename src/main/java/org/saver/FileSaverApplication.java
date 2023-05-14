package org.saver;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class FileSaverApplication {
	public static void main(String[] args) {
		Quarkus.run(args);
	}
}
