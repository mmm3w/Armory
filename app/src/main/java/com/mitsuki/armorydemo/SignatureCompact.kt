package com.mitsuki.armorydemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.lang.Exception
import java.security.MessageDigest
import javax.security.cert.X509Certificate


object SignatureCompact {

    @SuppressLint("PackageManagerGetSignatures")
    fun getSignature(context: Context): String {
        val cert: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            packageInfo.signingInfo.apkContentsSigners.let {
                if (it.isNotEmpty()) it[0].toByteArray() else null
            }
        } else {
            @Suppress("DEPRECATION")
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            @Suppress("DEPRECATION")
            packageInfo.signatures.let {
                if (it.isNotEmpty()) it[0].toByteArray() else null
            }
        }

        try {
            val x509: X509Certificate = X509Certificate.getInstance(cert)
            val md: MessageDigest = MessageDigest.getInstance("SHA1")
            return bytesToHex(md.digest(x509.encoded))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

}