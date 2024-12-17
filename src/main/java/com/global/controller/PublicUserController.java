package com.global.controller;


import com.global.dto.UserDto;
import com.global.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Log4j2
@RequiredArgsConstructor
public class PublicUserController {


    private final UserService userService;



    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
            Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );


    /**
     * {@code GET /users} : get all users with only the public information - calling this is allowed for anyone.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllPublicUsers( Pageable pageable) {
        log.debug("REST request to get all public User names");

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDto> page = userService.getAllPublicUsers(pageable);

        HttpHeaders headers = generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream()
                .map(Sort.Order::getProperty)
                .allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * Generate pagination HTTP headers for the current request.
     *
     * @param page the page information.
     * @return HttpHeaders containing pagination information.
     */
    private HttpHeaders generatePaginationHttpHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();

        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        int pageNumber = page.getNumber();
        int pageSize = page.getSize();
        long totalElements = page.getTotalElements();

        headers.add("X-Total-Count", String.valueOf(totalElements));
        headers.add("Link", createLinkHeader(baseUrl, pageNumber, pageSize, totalElements));

        return headers;
    }

    private String createLinkHeader(String baseUrl, int pageNumber, int pageSize, long totalElements) {
        StringBuilder linkHeader = new StringBuilder();

        if (pageNumber > 0) {
            linkHeader.append("<")
                    .append(baseUrl)
                    .append("?page=")
                    .append(pageNumber - 1)
                    .append("&size=")
                    .append(pageSize)
                    .append(">; rel=\"prev\", ");
        }

        if ((pageNumber + 1) * pageSize < totalElements) {
            linkHeader.append("<")
                    .append(baseUrl)
                    .append("?page=")
                    .append(pageNumber + 1)
                    .append("&size=")
                    .append(pageSize)
                    .append(">; rel=\"next\", ");
        }

        linkHeader.append("<")
                .append(baseUrl)
                .append("?page=0&size=")
                .append(pageSize)
                .append(">; rel=\"first\", ");

        long lastPage = (totalElements - 1) / pageSize;
        linkHeader.append("<")
                .append(baseUrl)
                .append("?page=")
                .append(lastPage)
                .append("&size=")
                .append(pageSize)
                .append(">; rel=\"last\"");

        return linkHeader.toString();
    }


    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/roles")
    public List<String> getRoles() {
        return userService.getRoles();
    }


}
