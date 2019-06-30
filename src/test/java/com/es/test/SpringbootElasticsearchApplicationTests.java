package com.es.test;

import com.es.entity.Article;
import com.es.entity.Book;
import com.es.mapper.BookMapper;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootElasticsearchApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    public void createIndex() {
        //1. 给ES中索引(保存)一个文档
        Article article = new Article();
        article.setId( 1 );
        article.setTitle( "好消息" );
        article.setAuthor( "张三" );
        article.setContent( "Hello World" );

        //2. 构建一个索引
        Index index = new Index.Builder( article ).index( "gf" ).type( "news" ).build();
        try {
            //3. 执行
            jestClient.execute( index );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void search() {
        //查询表达式
        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  }\n" +
                "}";

        //构建搜索功能
        Search search = new Search.Builder( query ).addIndex( "gf" ).addType( "book" ).build();

        try {
            //执行
            SearchResult result = jestClient.execute( search );
            System.out.println(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    BookMapper bookRepository;

    @Test
    public void createIndex2(){
        Book book = new Book();
        book.setId(1);
        book.setBookName("西游记");
        book.setAuthor( "吴承恩" );
        bookRepository.index( book );
    }

    @Test
    public void useFind() {
        List<Book> list = bookRepository.findByBookNameLike( "游" );
        for (Book book : list) {
            System.out.println(book);
        }

    }

}