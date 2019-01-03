package com.dono.foundation.utils;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.ObjectWritingException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.LockFile;
import org.eclipse.jgit.internal.storage.file.ObjectDirectory;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.RefWriter;

/**
 * Jgit To help Java developers who are building Application based on JGit.
 *
 */
public class JGitUtil 
{
	
	/*
	 * A dumb server that does not do on-the-fly pack generations must have 
	 * some auxiliary information files in $GIT_DIR/info and $GIT_OBJECT_DIRECTORY/info directories 
	 * to help clients discover what references and packs the server has. 
	 * This function generates such auxiliary files.
	 * 
	 * @param Git gitToUpdate - The Git instance to update it's server information. 
	 * 
	 */
	public static void updateServerInfo(Git gitToUpdate) throws Exception {

		if (gitToUpdate.getRepository() instanceof FileRepository) {
			final FileRepository fileRepo = (FileRepository) gitToUpdate.getRepository();

			RefWriter refWriter = new RefWriter(fileRepo.getRefDatabase().getRefs(RefDatabase.ALL)) {
				@Override
				protected void writeFile(String file, byte[] content) throws IOException {
					File path = new File(fileRepo.getDirectory(), file);
					JGitUtil.writeFile(path, content);
				}
			};

			refWriter.writeInfoRefs();

			ObjectDirectory objectDatabase = fileRepo.getObjectDatabase();
			final StringBuilder w = new StringBuilder();
			for (PackFile p : objectDatabase.getPacks()) {
				w.append("P ");
				w.append(p.getPackFile().getName());
				w.append('\n');
			}
			writeFile(new File(new File(objectDatabase.getDirectory(), "info"), "packs"), Constants.encodeASCII(w.toString()));
		}
	}
	
	private static void writeFile(File p, byte[] bin) throws IOException, ObjectWritingException {
		final LockFile lck = new LockFile(p);
		if (!lck.lock())
			throw new ObjectWritingException("Can't write " + p);
		try {
			lck.write(bin);
			if (!lck.commit())
				throw new ObjectWritingException("Can't write " + p);
		}
		catch (IOException ioe) {
			throw new ObjectWritingException("Can't write " + p);
		}
		finally {
			lck.unlock();
		}
	}
}
