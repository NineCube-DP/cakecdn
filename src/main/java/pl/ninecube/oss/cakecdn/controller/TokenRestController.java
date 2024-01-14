/* (C)2024 */
package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.model.dto.CreateTokenDto;
import pl.ninecube.oss.cakecdn.model.dto.TokenResponse;
import pl.ninecube.oss.cakecdn.service.ApplicationTokenService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Create, delete application token")
public class TokenRestController {

    private final ApplicationTokenService applicationTokenService;

    @PostMapping
    public TokenResponse registerToken(@RequestBody CreateTokenDto createTokenDto) {
        return applicationTokenService.createToken(createTokenDto);
    }

    @GetMapping
    public Set<TokenResponse> getProjectApplicationTokens(@RequestParam Long projectId) {
        return applicationTokenService.getTokens(projectId);
    }

    @DeleteMapping
    public void deleteToken(@RequestParam Long tokenId) {
        applicationTokenService.deleteToken(tokenId);
    }
}
