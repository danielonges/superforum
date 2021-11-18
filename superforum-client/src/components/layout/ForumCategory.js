import React, { useContext, useState, useEffect } from 'react';
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

const ForumCategory = () => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const [categoryForums, setCategoryForums] = useState([]);

    const location = useLocation();
    const { category } = location.state;

    const { getForumsByCategory, forums } = forumContext;
    const { user } = userContext;

    useEffect(() => {
        console.log('hello');
        if (category != null) {
            getForumsByCategory(category);
        }
        // eslint-disable-next-line
    }, [category]);

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <main>
                    <Grid container spacing={5}>
                        <Main title={category} forums={forums} />
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

export default ForumCategory;
