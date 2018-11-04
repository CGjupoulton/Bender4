package view;

import Bender4.Board;
import Bender4.Cell;
import com.codingame.game.Referee;
import com.codingame.gameengine.module.entities.*;

public class BoardView {
    private GraphicEntityModule graphics;
    public static final int CELL_SIZE = 48;
    public Group boardGroup;
    public Group functionsGroup;
    private static String[] wallSprites;
    private SpriteAnimation fry;

    public BoardView(Board board, GraphicEntityModule graphics) {
        wallSprites = Utils.loadSheet(graphics, "walls.png", CELL_SIZE, CELL_SIZE, 16, 4);
        this.graphics = graphics;

        boardGroup = graphics.createGroup();
        boardGroup.setScale(1080.0 / (board.height * CELL_SIZE));
        functionsGroup = graphics.createGroup().setX(1120);
        BufferedGroup gridGroup = graphics.createBufferedGroup();
        boardGroup.add(gridGroup);
        Group innerGroup = graphics.createGroup();
        gridGroup.add(innerGroup);

        Sprite background = graphics.createSprite().setImage("frame.png").setX(-40).setZIndex(-1).setAlpha(0.3);
        Sprite backgroundTop = graphics.createSprite().setImage("frameTop.png").setX(-40).setZIndex(3);

        Sprite zoidberg = graphics.createSprite().setImage("Zoidberg.png").setX(55).setY(130).setAlpha(0.05).setZIndex(3);
        functionsGroup.add(background);
        functionsGroup.add(backgroundTop);
        functionsGroup.add(zoidberg);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Sprite floor = Utils.createBoardSprite(graphics, "floor.png", x, y);
                innerGroup.add(floor);
                if (board.grid[x][y].isWall) {
                   Sprite wall = Utils.createBoardSprite(graphics, selectWall(board.grid, board.width, board.height, x, y), x, y);
                   innerGroup.add(wall);
                }
            }
        }

        String[] frySheet = Utils.loadSheet(graphics, "Fry.png", CELL_SIZE, CELL_SIZE, 2, 1);
        fry = Utils.createAnimation(graphics, frySheet)
                .setX(board.target.x * CELL_SIZE)
                .setY(board.target.y * CELL_SIZE);
        boardGroup.add(fry);
    }

    public void win() {
        String[] victorySheet = Utils.loadSheet(graphics, "FryVictory.png", BoardView.CELL_SIZE, BoardView.CELL_SIZE, 5, 5);
        fry.setImages(victorySheet).setDuration(Referee.FRAME_DURATION * 2).stop().setLoop(false);
        fry.setX(fry.getX() - 5);
        graphics.commitEntityState(0.101, fry);
        fry.start();
        graphics.commitEntityState(0.102, fry);
    }

    private static String selectWall(Cell[][] grid, int width, int height, int x, int y) {
        int index = 0;
        if (y > 0 && grid[x][y - 1].isWall) index += 1;
        if (y + 1 < height && grid[x][y + 1].isWall) index += 2;
        if (x > 0 && grid[x - 1][y].isWall) index += 8;
        if (x + 1 < width && grid[x + 1][y].isWall) index += 4;
        return wallSprites[index];
    }
}
