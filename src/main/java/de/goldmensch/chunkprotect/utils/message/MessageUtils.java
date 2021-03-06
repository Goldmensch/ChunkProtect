
/*
 * Copyright (C) 2021 Nick Hensel
 * This file is part of Smartclans.
 *
 * Smartclans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * Smartclans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package de.goldmensch.chunkprotect.utils.message;

import java.util.List;

public class MessageUtils {

    public static String formatList(List<String> list) {
        StringBuilder formattedList = new StringBuilder();
        for(int i = 0; i < list.size(); i++) {
            formattedList.append(list.get(i));
            if((i+1) != list.size()) {
                formattedList.append(", ");
            }
        }
        return formattedList.toString();
    }

}
