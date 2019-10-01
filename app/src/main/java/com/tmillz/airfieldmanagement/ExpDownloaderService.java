package com.tmillz.airfieldmanagement;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class ExpDownloaderService extends DownloaderService {
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp4Rt+IN2cwH+sdHAj8QljplCyEdSHAtMaCIOpdCma0iKLCToYGV4hJT3mIgoOmvl9QQVoE0qltthK2FgviSORVJi41ERs/I3E3swr0oz9ArSXZiKR1o0/IgC4xlCIzZayloGX2CcaHTfRHyBwLZIK5RYvcSoNKLj3qu1ImQrVk5xn+6w9caipz3p6XR5oF/licWR5WPyGM8mybbklBGdPSr63vIEQ2YhmxBlf8a5K/HdLBTtHD1l6n3WeLyR/XCuMywpKi0WmUquGg9lztCKSIUTeT/BVPn9MgNC8U5xl3jg9w3DWDQhPsQ5bcohZaMT/3bJXK1nAwenxiaEVZ8FYwIDAQAB";
    public static final byte[] SALT = new byte[] { 2, 42, -17, -1, 54, 100
            -110, -12, 23, 2, -8, -1, 9, 5, -103, -107, -36, 45, -1, 85
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return ExpAlarmReceiver.class.getName();
    }
}
