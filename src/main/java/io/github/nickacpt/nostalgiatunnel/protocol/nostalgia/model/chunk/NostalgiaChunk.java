package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.chunk;

public class NostalgiaChunk {

    private final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];

    public ExtendedBlockStorage[] getStorageArrays() {
        return storageArrays;
    }

    private final byte[] blockBiomeArray = new byte[256];

    public void fillChunk(byte[] decompressedData, int primary, int add, boolean isFull, boolean hasSkyLight) {
        Object object;
        int n;
        int n2 = 0;
        for (n = 0; n < this.storageArrays.length; ++n) {
            if ((primary & 1 << n) != 0) {
                if (this.storageArrays[n] == null) {
                    this.storageArrays[n] = new ExtendedBlockStorage(hasSkyLight);
                }
                object = this.storageArrays[n].getBlockLSBArray();
                System.arraycopy(decompressedData, n2, object, 0, ((byte[])object).length);
                n2 += ((byte[])object).length;
                continue;
            }
            if (!isFull || this.storageArrays[n] == null) continue;
            this.storageArrays[n] = null;
        }
        for (n = 0; n < this.storageArrays.length; ++n) {
            if ((primary & 1 << n) == 0 || this.storageArrays[n] == null) continue;
            object = this.storageArrays[n].getMetadataArray();
            System.arraycopy(decompressedData, n2, ((NibbleArray) object).getData(), 0, ((NibbleArray) object).getData().length);
            n2 += ((NibbleArray) object).getData().length;
        }
        for (n = 0; n < this.storageArrays.length; ++n) {
            if ((primary & 1 << n) == 0 || this.storageArrays[n] == null) continue;
            object = this.storageArrays[n].getBlocklightArray();
            System.arraycopy(decompressedData, n2, ((NibbleArray) object).getData(), 0, ((NibbleArray) object).getData().length);
            n2 += ((NibbleArray) object).getData().length;
        }
        if (hasSkyLight) {
            for (n = 0; n < this.storageArrays.length; ++n) {
                if ((primary & 1 << n) == 0 || this.storageArrays[n] == null) continue;
                object = this.storageArrays[n].getSkylightArray();
                System.arraycopy(decompressedData, n2, ((NibbleArray) object).getData(), 0, ((NibbleArray) object).getData().length);
                n2 += ((NibbleArray) object).getData().length;
            }
        }
        for (n = 0; n < this.storageArrays.length; ++n) {
            if ((add & 1 << n) != 0) {
                if (this.storageArrays[n] == null) {
                    n2 += 2048;
                    continue;
                }
                object = this.storageArrays[n].getBlockMSBArray();
                if (object == null) {
                    object = this.storageArrays[n].createBlockMSBArray();
                }
                System.arraycopy(decompressedData, n2, ((NibbleArray) object).getData(), 0, ((NibbleArray) object).getData().length);
                n2 += ((NibbleArray) object).getData().length;
                continue;
            }
            if (!isFull || this.storageArrays[n] == null || this.storageArrays[n].getBlockMSBArray() == null) continue;
            this.storageArrays[n].clearMSBArray();
        }
        if (isFull) {
            System.arraycopy(decompressedData, n2, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            n2 += this.blockBiomeArray.length;
        }
    }
}
