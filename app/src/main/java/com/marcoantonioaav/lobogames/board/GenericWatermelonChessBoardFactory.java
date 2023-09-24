package com.marcoantonioaav.lobogames.board;

import androidx.core.content.ContextCompat;
import com.marcoantonioaav.lobogames.R;
import com.marcoantonioaav.lobogames.application.LoBoGames;
import com.marcoantonioaav.lobogames.position.Connection;
import com.marcoantonioaav.lobogames.position.Coordinate;
import com.marcoantonioaav.lobogames.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericWatermelonChessBoardFactory {

    private GenericWatermelonChessBoardFactory() {
    }

    private static final double PADDING_PERCENTAGE = 0.03;
    private static final double POSITION_RADIUS_SCALE = (double) 1 / 25;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
    private static final List<Position> POSITIONS = new ArrayList<>();

    static {
        Position top = new Position(new Coordinate(389, 14), "cima", 0);
        Position topLeft = new Position(new Coordinate(255, 39), "cima esquerda", 1);
        Position topRight = new Position(new Coordinate(522, 39), "cima direita", 2);
        Position topCenter = new Position(new Coordinate(389, 159), "cima centro", 3);

        Position leftTop = new Position(new Coordinate(40, 252), "esquerda cima", 4);
        Position centerTop = new Position(new Coordinate(389, 252), "centro topo", 5);
        Position rightTop = new Position(new Coordinate(735, 252), "direita cima", 6);

        Position left = new Position(new Coordinate(14, 385), "esquerda", 7);
        Position leftCenter = new Position(new Coordinate(162, 385), "esquerda centro", 8);
        Position centerLeft = new Position(new Coordinate(254, 385), "centro esquerda", 9);

        Position center = new Position(new Coordinate(389, 385), "centro", 10);

        Position centerRight = new Position(new Coordinate(522, 385), "centro direita", 11);
        Position rightCenter = new Position(new Coordinate(614, 385), "direita centro", 12);
        Position right = new Position(new Coordinate(759, 385), "direita", 13);

        Position leftBottom = new Position(new Coordinate(40, 519), "esquerda baixo", 14);
        Position centerBottom = new Position(new Coordinate(389, 519), "centro baixo", 15);
        Position rightBottom = new Position(new Coordinate(735, 519), "direita baixo", 16);

        Position bottomCenter = new Position(new Coordinate(389, 612), "baixo centro", 17);
        Position bottomLeft = new Position(new Coordinate(255, 735), "baixo esquerda", 18);
        Position bottomRight = new Position(new Coordinate(522, 735), "baixo direita", 19);
        Position bottom = new Position(new Coordinate(389, 758), "baixo", 20);


        POSITIONS.addAll(
                Arrays.asList(
                        top, topLeft, topRight, topCenter,
                        leftTop, centerTop, rightTop,
                        left, leftCenter, centerLeft,
                        center,
                        centerRight, rightCenter, right,
                        leftBottom, centerBottom, rightBottom,
                        bottomCenter, bottomLeft, bottomRight, bottom
                ));


        CONNECTIONS.addAll(Arrays.asList(
                new Connection(top, topCenter), new Connection(bottom, bottomCenter),
                new Connection(left, leftCenter), new Connection(right, rightCenter),

                new Connection(topCenter, centerTop), new Connection(bottomCenter, centerBottom),
                new Connection(leftCenter, centerLeft), new Connection(rightCenter, centerRight),

                new Connection(center, centerRight), new Connection(center, centerLeft),
                new Connection(center, centerBottom), new Connection(center, centerTop),

                new Connection(topLeft, top,
                        Arrays.asList(
                                new Coordinate(281, 30),
                                new Coordinate(308, 23),
                                new Coordinate(335, 18),
                                new Coordinate(362, 15)
                        )
                ),
                new Connection(topLeft, topCenter,
                        Arrays.asList(
                                new Coordinate(265, 65),
                                new Coordinate(277, 99),
                                new Coordinate(299, 126),
                                new Coordinate(321, 142),
                                new Coordinate(344, 152),
                                new Coordinate(366, 158)
                        )
                ),
                new Connection(topRight, top,
                        Arrays.asList(
                                new Coordinate(495, 30),
                                new Coordinate(468, 23),
                                new Coordinate(442, 18),
                                new Coordinate(415, 15)
                        )
                ),
                new Connection(topRight, topCenter,
                        Arrays.asList(
                                new Coordinate(517, 65),
                                new Coordinate(499, 99),
                                new Coordinate(477, 126),
                                new Coordinate(455, 142),
                                new Coordinate(433, 152),
                                new Coordinate(411, 158)
                        )
                ),

                new Connection(bottomLeft, bottom,
                        Arrays.asList(
                                new Coordinate(281, 743),
                                new Coordinate(308, 750),
                                new Coordinate(335, 756),
                                new Coordinate(362, 759)
                        )),
                new Connection(bottomLeft, bottomCenter,
                        Arrays.asList(
                                new Coordinate(265, 693),
                                new Coordinate(277, 672),
                                new Coordinate(299, 646),
                                new Coordinate(321, 629),
                                new Coordinate(344, 619),
                                new Coordinate(366, 613)
                        )
                ),
                new Connection(bottomRight, bottom,
                        Arrays.asList(
                                new Coordinate(495, 743),
                                new Coordinate(468, 750),
                                new Coordinate(442, 756),
                                new Coordinate(415, 759)
                        )),

                new Connection(bottomRight, bottomCenter,
                        Arrays.asList(
                                new Coordinate(517, 707),
                                new Coordinate(499, 669),
                                new Coordinate(477, 645),
                                new Coordinate(455, 629),
                                new Coordinate(433, 619),
                                new Coordinate(411, 613)
                        )),

                new Connection(centerTop, centerLeft,
                        Arrays.asList(
                                new Coordinate(359, 254),
                                new Coordinate(346, 258),
                                new Coordinate(331, 264),
                                new Coordinate(306, 279),
                                new Coordinate(301, 284),
                                new Coordinate(286, 299),
                                new Coordinate(270, 322),
                                new Coordinate(261, 342),
                                new Coordinate(256, 364)
                        )),
                new Connection(centerTop, centerRight,
                        Arrays.asList(
                                new Coordinate(415, 254),
                                new Coordinate(430, 258),
                                new Coordinate(445, 264),
                                new Coordinate(469, 279),
                                new Coordinate(476, 284),
                                new Coordinate(491, 299),
                                new Coordinate(506, 322),
                                new Coordinate(515, 342),
                                new Coordinate(521, 364)
                        )),
                new Connection(centerBottom, centerLeft,
                        Arrays.asList(
                                new Coordinate(359, 517),
                                new Coordinate(346, 513),
                                new Coordinate(331, 507),
                                new Coordinate(306, 492),
                                new Coordinate(290, 477),
                                new Coordinate(273, 454),
                                new Coordinate(263, 434),
                                new Coordinate(257, 412),
                                new Coordinate(255, 391)
                        )),
                new Connection(centerBottom, centerRight,
                        Arrays.asList(
                                new Coordinate(413, 517),
                                new Coordinate(432, 513),
                                new Coordinate(445, 507),
                                new Coordinate(470, 492),
                                new Coordinate(485, 477),
                                new Coordinate(504, 454),
                                new Coordinate(513, 434),
                                new Coordinate(520, 412),
                                new Coordinate(522, 391)
                        )),

                new Connection(leftTop, left,
                        Arrays.asList(
                                new Coordinate(32, 272),
                                new Coordinate(25, 299),
                                new Coordinate(18, 330),
                                new Coordinate(16, 358)
                        )),
                new Connection(leftTop, leftCenter,
                        Arrays.asList(
                                new Coordinate(55, 254),
                                new Coordinate(70, 258),
                                new Coordinate(86, 264),
                                new Coordinate(101, 279),
                                new Coordinate(116, 284),
                                new Coordinate(131, 299),
                                new Coordinate(147, 322),
                                new Coordinate(155, 342),
                                new Coordinate(162, 364)
                        )
                ),
                new Connection(leftTop, topLeft,
                        Arrays.asList(
                                new Coordinate(67, 197),
                                new Coordinate(94, 159),
                                new Coordinate(121, 128),
                                new Coordinate(147, 103),
                                new Coordinate(174, 81),
                                new Coordinate(201, 64),
                                new Coordinate(228, 50)
                        )
                ),
                new Connection(leftBottom, left,
                        Arrays.asList(
                                new Coordinate(32, 499),
                                new Coordinate(25, 470),
                                new Coordinate(18, 428),
                                new Coordinate(16, 398)
                        )),
                new Connection(leftBottom, leftCenter,
                        Arrays.asList(
                                new Coordinate(55, 517),
                                new Coordinate(70, 513),
                                new Coordinate(86, 507),
                                new Coordinate(101, 492),
                                new Coordinate(116, 477),
                                new Coordinate(131, 454),
                                new Coordinate(147, 434),
                                new Coordinate(155, 412),
                                new Coordinate(162, 391)
                        )
                ),

                new Connection(leftBottom, bottomLeft,
                        Arrays.asList(
                                new Coordinate(67, 577),
                                new Coordinate(94, 614),
                                new Coordinate(121, 644),
                                new Coordinate(147, 670),
                                new Coordinate(174, 690),
                                new Coordinate(201, 709),
                                new Coordinate(228, 722)
                        )

                ),

                new Connection(rightTop, right,
                        Arrays.asList(
                                new Coordinate(743, 272),
                                new Coordinate(750, 299),
                                new Coordinate(756, 330),
                                new Coordinate(758, 358)
                        )),

                new Connection(rightTop, rightCenter,
                        Arrays.asList(
                                new Coordinate(717, 254),
                                new Coordinate(705, 258),
                                new Coordinate(690, 264),
                                new Coordinate(667, 279),
                                new Coordinate(660, 284),
                                new Coordinate(646, 299),
                                new Coordinate(630, 322),
                                new Coordinate(621, 342),
                                new Coordinate(616, 364)
                        )),
                new Connection(rightTop, topRight,
                        Arrays.asList(
                                new Coordinate(710, 197),
                                new Coordinate(684, 159),
                                new Coordinate(655, 128),
                                new Coordinate(630, 103),
                                new Coordinate(603, 81),
                                new Coordinate(576, 64),
                                new Coordinate(550, 50)
                        )
                ),
                new Connection(rightBottom, right,
                        Arrays.asList(
                                new Coordinate(743, 498),
                                new Coordinate(750, 469),
                                new Coordinate(756, 437),
                                new Coordinate(758, 415)
                        )),
                new Connection(rightBottom, rightCenter,
                        Arrays.asList(
                                new Coordinate(722, 517),
                                new Coordinate(707, 513),
                                new Coordinate(693, 507),
                                new Coordinate(667, 492),
                                new Coordinate(651, 477),
                                new Coordinate(633, 454),
                                new Coordinate(623, 434),
                                new Coordinate(617, 412),
                                new Coordinate(614, 391)
                        )),
                new Connection(rightBottom, bottomRight,
                        Arrays.asList(
                                new Coordinate(710, 574),
                                new Coordinate(684, 610),
                                new Coordinate(655, 646),
                                new Coordinate(630, 669),
                                new Coordinate(603, 692),
                                new Coordinate(576, 708),
                                new Coordinate(550, 722)
                        ))
        ));
    }

    public static GenericBoard from(List<Integer> initialPlayerIds) {
        if (initialPlayerIds.size() != POSITIONS.size()) {
            throw new IllegalArgumentException("Expected initial playerIds of size " + POSITIONS.size());
        }

        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < POSITIONS.size(); i++) {
            Position position = POSITIONS.get(i).copy();
            position.setPlayerId(initialPlayerIds.get(i));
            positions.add(position);
        }

        List<Connection> connections = new ArrayList<>();
        for (Connection connection : CONNECTIONS) {
            connections.add(connection.copy());
            connections.add(connection.reverseCopy());
        }

        return new GenericBoard(
                ContextCompat.getDrawable(LoBoGames.getAppContext(), R.drawable.watermelon_chess),
                PADDING_PERCENTAGE,
                PADDING_PERCENTAGE,
                POSITION_RADIUS_SCALE,
                positions,
                connections
        );
    }
}