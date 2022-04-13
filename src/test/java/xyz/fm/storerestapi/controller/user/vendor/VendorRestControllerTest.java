package xyz.fm.storerestapi.controller.user.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.repository.user.vendor.VendorApiRepository;
import xyz.fm.storerestapi.repository.user.vendor.VendorManagerApiRepository;
import xyz.fm.storerestapi.service.user.vendor.VendorService;

@WebMvcTest(VendorRestController.class)
abstract class VendorRestControllerTest {

    protected static final String BASE_URL = "/api/vendor";
    protected static final ObjectMapper om = new ObjectMapper();

    @Autowired protected MockMvc mvc;
    @MockBean protected VendorService vendorService;
    @MockBean protected VendorApiRepository vendorApiRepository;
    @MockBean protected VendorManagerApiRepository vendorManagerApiRepository;
    @SpyBean protected JwtTokenUtil jwtTokenUtil;
}