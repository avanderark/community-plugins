/*
 * @(#)DarWriter.java     27 Jul 2011
 *
 * Copyright © 2010 Andrew Phillips.
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package ext.deployit.community.cli.plainarchive.dar;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.io.Closeables.close;

import java.io.File;
import java.io.IOException;
import java.util.jar.Manifest;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileOutputStream;
import de.schlichtherle.truezip.fs.FsSyncException;

/**
 * @author aphillips
 * @since 27 Jul 2011
 *
 */
public class DarWriter {
    public static final String DAR_EXTENSION = "dar";
    public static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DarWriter.class);
    
    public static void addManifest(@Nonnull File dar, @Nonnull Manifest manifest) 
            throws IOException {
        TFile manifestFile = new TFile(dar, MANIFEST_PATH);
        checkArgument(!(manifestFile.exists()), 
                "DAR '%s' already contains a manifest at '%s'", dar, MANIFEST_PATH);
        
        TFileOutputStream out = new TFileOutputStream(manifestFile);
        try {
            LOGGER.debug("Opened output stream to write manifest '{}' to '{}'",
                    MANIFEST_PATH, dar);
            manifest.write(out);
            LOGGER.debug("Manifest '{}' written to '{}'", MANIFEST_PATH, dar);
        } finally {
            close(out, false);
        }
    }
    
    public static void flush(@Nonnull File dar) throws FsSyncException {
        LOGGER.debug("Flushing changes to DAR '{}'", dar);
        TFile.umount(new TFile(dar));
    }
}
