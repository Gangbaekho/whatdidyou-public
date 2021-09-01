package com.nuitblanche.whatdidyou;

import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.project.ProjectRepository;
import com.nuitblanche.whatdidyou.domain.user.AuthProvider;
import com.nuitblanche.whatdidyou.domain.user.Role;
import com.nuitblanche.whatdidyou.domain.user.User;
import com.nuitblanche.whatdidyou.domain.user.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LazyLoadingTest {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup(){
            User user = User.builder()
                    .name("tester")
                    .email("tester@test.com")
                    .password(passwordEncoder.encode("test"))
                    .provider(AuthProvider.local)
                    .role(Role.USER)
                    .providerId("test")
                    .emailVerified(false)
                    .imageUrl("test")
                    .build();

            userRepository.save(user);

            Project project = Project.builder()
                    .title("title")
                    .build();

            project.updateUser(user);
            projectRepository.save(project);

    }

    @After
    public void cleanup(){
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getUser(){

        EntityManager em = factory.createEntityManager();
        User user = userRepository.findAll().get(0);
        try{
            em.getTransaction().begin();
            User selectedUser = em.find(User.class,user.getId());
            List<Project> projects = selectedUser.getProjects();
            for(Project project : projects){
                System.out.println(project);
            }
            em.getTransaction().commit();
        } catch(Exception e){
            em.getTransaction().rollback();
        } finally{
            em.close();
        }

    }

}
