/*
 * Copyright (c) 2020-2021 Dirt Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.chunk

import kotlin.experimental.and
import kotlin.experimental.or

class NibbleArray {
    val data: ByteArray
    private val depthBits: Int
    private val depthBitsPlusFour: Int

    constructor(i: Int, j: Int) {
        data = ByteArray(i shr 1)
        depthBits = j
        depthBitsPlusFour = j + 4
    }

    constructor(bs: ByteArray, i: Int) {
        data = bs
        depthBits = i
        depthBitsPlusFour = i + 4
    }

    operator fun get(i: Int, j: Int, k: Int): Int {
        val n = j shl depthBitsPlusFour or (k shl depthBits) or i
        val n2 = n shr 1
        val n3 = n and 1
        return if (n3 == 0) {
            (data[n2] and 0xF).toInt()
        } else data[n2].toInt() shr 4 and 0xF
    }

    operator fun set(i: Int, j: Int, k: Int, l: Int) {
        val n = j shl depthBitsPlusFour or (k shl depthBits) or i
        val n2 = n shr 1
        val n3 = n and 1
        data[n2] =
            if (n3 == 0) (data[n2] and 0xF0.toByte() or l.toByte() and 0xF) else (((data[n2] and 0xF or ((l and 0xF).toByte())).toInt() shl 4).toByte())
    }
}