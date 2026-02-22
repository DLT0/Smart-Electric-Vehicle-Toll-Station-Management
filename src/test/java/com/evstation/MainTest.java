package com.evstation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cơ bản để xác nhận môi trường Maven + JUnit 5 hoạt động.
 */
class MainTest {

    @Test
    void applicationStartsSuccessfully() {
        // Smoke test: đảm bảo main() không throw exception
        assertDoesNotThrow(() -> Main.main(new String[] {}));
    }
}
