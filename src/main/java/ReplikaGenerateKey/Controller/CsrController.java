package ReplikaGenerateKey.Controller;

import ReplikaGenerateKey.entity.CsrRequest;
import ReplikaGenerateKey.service.CsrService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csr")
public class CsrController {
    public final CsrService csrService;

    public CsrController(CsrService csrService) {
        this.csrService = csrService;
    }

    @PostMapping
    public String create(@RequestBody CsrRequest request) throws Exception {
        String response = csrService.genCsr(request.getKeyAlias(),request.getSubjectInfo());

        return response;
    }
}

