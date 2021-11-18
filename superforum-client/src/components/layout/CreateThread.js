import React, { useContext, useEffect, useState } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import ListItemIcon from '@mui/material/ListItemIcon';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Footer from '../forum/Footer';
import AlertBar from './AlertBar';
import OutlinedInput from '@mui/material/OutlinedInput';

import AlertContext from '../../context/alert/alertContext';
import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';
import { Button, Card, MenuItem, TextField } from '@mui/material';
import { Box } from '@mui/system';

const theme = createTheme();

const CreateThread = (props) => {
    const alertContext = useContext(AlertContext);
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { setAlert } = alertContext;
    const { createForumThread, forum, forumCategories, getAllForumCategories } =
        forumContext;
    const { user } = userContext;

    const [selectValue, setSelectValue] = useState();
    const [newThread, setNewThread] = useState({
        title: '',
        post: ''
    });

    const { title, post } = newThread;

    useEffect(() => {
        if (forumCategories == null) {
            getAllForumCategories();
        }
        // eslint-disable-next-line
    }, [JSON.stringify(forumCategories)]);

    const onChange = (e) => {
        setNewThread({ ...newThread, [e.target.name]: e.target.value });
    };

    const onSubmit = (e) => {
        e.preventDefault();
        if (title === '') {
            setAlert('Please fill in title', 'error');
        } else {
            createForumThread(
                {
                    title,
                    owner: user,
                    posts:
                        post.length > 0
                            ? [
                                  {
                                      content: post
                                  }
                              ]
                            : null
                },
                forum.id
            );
            props.history.push('/forum');
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <main>
                    <Typography variant='h6' gutterBottom>
                        Create Thread
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Typography
                        variant='subtitle1'
                        gutterBottom
                        color='text.secondary'
                    >
                        Thread Details
                    </Typography>
                    <AlertBar />
                    <Box
                        component='form'
                        noValidate
                        sx={{ mt: 3 }}
                        onSubmit={onSubmit}
                    >
                        <Grid container spacing={3}>
                            <Grid item xs={12}>
                                <TextField
                                    name='title'
                                    required
                                    fullWidth
                                    label='Title'
                                    autoFocus
                                    onChange={onChange}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label='First Post Content'
                                    name='post'
                                    onChange={onChange}
                                    multiline
                                    rows={4}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type='submit'
                            variant='contained'
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Create Thread
                        </Button>
                    </Box>
                </main>
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};
export default CreateThread;
