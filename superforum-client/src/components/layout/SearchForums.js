import React, { useContext, useEffect } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import GitHubIcon from '@mui/icons-material/GitHub';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Sidebar from '../forum/Sidebar';
import Footer from '../forum/Footer';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';

import { useLocation } from 'react-router';

const theme = createTheme();

const SearchForums = () => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const location = useLocation();
    const { title } = location.state;

    const { getForumsByTitle, forums } = forumContext;
    const { user } = userContext;

    useEffect(() => {
        if (title != null) {
            getForumsByTitle(title);
        }
        // eslint-disable-next-line
    }, [title]);

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <main>
                    <Grid container spacing={5}>
                        <Main
                            title={
                                title.length > 0
                                    ? `All forums with title "${title}"`
                                    : 'All forums'
                            }
                            forums={forums}
                        />
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
export default SearchForums;
