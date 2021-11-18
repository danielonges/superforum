import React, { useContext, useEffect } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Pagination from '@mui/material/Pagination';
import Typography from '@mui/material/Typography';
import GitHubIcon from '@mui/icons-material/GitHub';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Footer from '../forum/Footer';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';

const theme = createTheme();

const Home = () => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { forumThreads, getUserRecentForumThreads, forums, getAllForums } =
        forumContext;
    const { user } = userContext;

    useEffect(() => {
        if (user != null) {
            getUserRecentForumThreads(user.id);
        }
        getAllForums();
        // eslint-disable-next-line
    }, [JSON.stringify(forumThreads), JSON.stringify(forums)]);

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <Typography
                    variant='subtitle1'
                    color='text.secondary'
                    sx={{ mb: 2 }}
                >{`Welcome, ${user.username}!`}</Typography>
                <main>
                    <Typography variant='h6' gutterBottom>
                        My recent threads
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Grid container spacing={4}>
                        {forumThreads != null &&
                            (forumThreads.length > 0 ? (
                                forumThreads.map((forumThread, i) => (
                                    <ForumThreadCard
                                        key={i}
                                        forumThread={forumThread}
                                    />
                                ))
                            ) : (
                                <Grid item xs={12}>
                                    <Typography
                                        variant='subtitle1'
                                        color='text.secondary'
                                        gutterBottom
                                        sx={{ margin: 1 }}
                                    >
                                        No recent threads!
                                    </Typography>
                                </Grid>
                            ))}
                    </Grid>
                    <Grid
                        container
                        spacing={5}
                        sx={{ mt: 3 }}
                        alignItems='center'
                    >
                        <Main title='All forums' forums={forums} />
                        {/* todo: implement pagination */}
                        <Grid item>
                            {/* <Pagination count={10} align='center' /> */}
                        </Grid>
                    </Grid>
                </main>
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};
export default Home;
