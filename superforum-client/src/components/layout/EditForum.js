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

import AlertContext from '../../context/alert/alertContext';
import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';
import { Button, MenuItem, TextField } from '@mui/material';
import { Box } from '@mui/system';

const theme = createTheme();

const EditForum = (props) => {
    const alertContext = useContext(AlertContext);
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const { setAlert } = alertContext;
    const { updateForum, forum, forumCategories, getAllForumCategories } =
        forumContext;
    const { user } = userContext;

    const [selectValue, setSelectValue] = useState();
    const [updatedForum, setUpdatedForum] = useState({
        title: forum.title,
        description: forum.description,
        category: forum.forumCategory.category
    });

    const { title, description, category } = updatedForum;

    const SET_NEW_CATEGORY = 'SET_NEW_CATEGORY';

    useEffect(() => {
        if (forumCategories == null) {
            getAllForumCategories();
        }
        // eslint-disable-next-line
    }, [JSON.stringify(forumCategories)]);

    const onChangeNewCategory = (e) => {
        setUpdatedForum({ ...updatedForum, category: e.target.value });
    };

    const onChange = (e) => {
        if (e.target.name === 'category') {
            setSelectValue(e.target.value);
        }
        if (e.target.value !== SET_NEW_CATEGORY) {
            setUpdatedForum({
                ...updatedForum,
                [e.target.name]: e.target.value
            });
        }
    };

    const onSubmit = (e) => {
        e.preventDefault();
        if (title === '' || description === '' || category === '') {
            setAlert('Please fill in all fields', 'error');
        } else {
            updateForum(
                {
                    id: forum.id,
                    title,
                    description,
                    forumCategory: {
                        category
                    }
                },
                user.id
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
                        Edit Forum
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Typography
                        variant='subtitle1'
                        gutterBottom
                        color='text.secondary'
                    >
                        Forum Details
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
                                    defaultValue={forum.title}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    label='Description'
                                    name='description'
                                    onChange={onChange}
                                    defaultValue={forum.description}
                                />
                            </Grid>
                            <Grid item xs={4}>
                                <TextField
                                    required
                                    fullWidth
                                    select
                                    name='category'
                                    label='Category'
                                    onChange={onChange}
                                    defaultValue={forum.forumCategory.category}
                                >
                                    {forumCategories != null &&
                                        forumCategories.map((category) => (
                                            <MenuItem
                                                key={category.id}
                                                value={category.category}
                                            >
                                                {category.category}
                                            </MenuItem>
                                        ))}
                                    <MenuItem
                                        value={SET_NEW_CATEGORY}
                                        color='text.secondary'
                                    >
                                        {/* <ListItemIcon>
                                            <AddIcon fontSize='small' />
                                        </ListItemIcon> */}
                                        + New Category
                                    </MenuItem>
                                </TextField>
                            </Grid>
                            {selectValue === SET_NEW_CATEGORY && (
                                <Grid item xs={8}>
                                    <TextField
                                        fullWidth
                                        name='New Category'
                                        label='New Category'
                                        onChange={onChangeNewCategory}
                                    />
                                </Grid>
                            )}
                        </Grid>
                        <Button
                            type='submit'
                            variant='contained'
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Update Forum
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
export default EditForum;
