package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerScore {
    private String player;
    private int score;

    @Override
    public boolean equals(Object obj) {
        // 1. 检查引用是否相等
        if (this == obj) {
            return true;
        }

        // 2. 检查对象是否是同一类型
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 3. 强制类型转换
        PlayerScore other = (PlayerScore) obj;

        // 4. 比较id字段
        return Objects.equals(player, other.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, score);
    }
}
