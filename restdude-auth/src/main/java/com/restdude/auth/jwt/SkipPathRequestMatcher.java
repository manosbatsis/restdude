/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.jwt;

import lombok.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Skips the given path(s). Negates the result of the enclosed {@link OrRequestMatcher}
 * which is initialized with the given paths after wrapping each one within a {@link AntPathRequestMatcher} instance
 */
public class SkipPathRequestMatcher extends NegatedRequestMatcher {

    /**
     * Creates a new instance
     *
     * @param pathToSkip the path to skip
     */
    public SkipPathRequestMatcher(@NonNull String pathToSkip) {
        this(new String[]{pathToSkip});
    }

    /**
     * Creates a new instance
     *
     * @param pathsToSkip the paths to skip
     */
    public SkipPathRequestMatcher(@NonNull String[] pathsToSkip) {
        this(Arrays.asList(pathsToSkip));
    }

    /**
     * Creates a new instance
     *
     * @param pathsToSkip the paths to skip
     */
    public SkipPathRequestMatcher(@NonNull List<String> pathsToSkip) {
        super(new OrRequestMatcher(pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList())));
    }

}
